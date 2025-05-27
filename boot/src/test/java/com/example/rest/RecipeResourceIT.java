package com.example.rest;

import com.example.IntegrationTest;
import com.example.config.TestSecurityConfig;
import com.example.domain.model.Ingredient;
import com.example.domain.model.Recipe;
import com.example.domain.model.RecipeCategory;
import com.example.userinterface.dto.RecipeDTO;
import jakarta.persistence.EntityManager;
import com.example.userinterface.mapper.RecipeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.example.application.port.out.IngredientRepository;
import com.example.application.port.out.RecipeRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RecipeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class RecipeResourceIT {

    private static final String API_KEY_HEADER = "x-api-key";
    private static final String APIKey = TestSecurityConfig.TEST_API_KEY;

    private static final RecipeCategory DEFAULT_VEGETARIAN = RecipeCategory.nonvegetarian;
    private static final RecipeCategory UPDATED_VEGETARIAN = RecipeCategory.vegetarian;

    private static final Integer DEFAULT_SERVINGS = 1;
    private static final Integer UPDATED_SERVINGS = 2;

    private static final String DEFAULT_INSTRUCTIONS = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTIONS = "BBBBBBBBBB";

    private static final String DEFAULT_INGREDIENT = "CCCCCCCCCC";
    private static final String UPDATED_INGREDIENT = "DDDDDDDDDD";

    private static final String ENTITY_API_URL = "/api/recipes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt());

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecipeMockMvc;

    private Recipe recipe;

    private Set<Ingredient> IngredientsSet = new HashSet<>();

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recipe createEntity(EntityManager em, Set<Ingredient> IngredientsSet) {
        Recipe i = new Recipe();
        i.setCategory(DEFAULT_VEGETARIAN);
        i.setServings(DEFAULT_SERVINGS);
        i.setInstructions(DEFAULT_INSTRUCTIONS);
        i.setIngredients(IngredientsSet);
        return i;

    }


    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingredient createIngredientEntity(EntityManager em) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(DEFAULT_INGREDIENT);
        return ingredient;
    }

    @BeforeEach
    public void initTest() {
        // Create and save the ingredient first
        Ingredient ingredient = createIngredientEntity(em);
        ingredient = ingredientRepository.save(ingredient);
        
        // Create the recipe with the saved ingredient
        recipe = createEntity(em, Collections.singleton(ingredient));
        
        // Save the recipe to ensure the relationship is established
        recipe = recipeRepository.save(recipe);
    }

    @Test
    @Transactional
    void createRecipe() throws Exception {
        int databaseSizeBeforeCreate = recipeRepository.findAll(0,10).size();
        
        // Create a new recipe with the existing ingredient
        Recipe newRecipe = createEntity(em, recipe.getIngredients());
        RecipeDTO recipeDTO = recipeMapper.toDto(newRecipe);
        
        restRecipeMockMvc
                .perform(post(ENTITY_API_URL)
                        .header(API_KEY_HEADER, APIKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
                .andExpect(status().isCreated());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll(0,10);
        assertThat(recipeList).hasSize(databaseSizeBeforeCreate + 1);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getCategory()).isEqualTo(DEFAULT_VEGETARIAN);
        assertThat(testRecipe.getServings()).isEqualTo(DEFAULT_SERVINGS);
        assertThat(testRecipe.getInstructions()).isEqualTo(DEFAULT_INSTRUCTIONS);
        assertThat(testRecipe.getIngredients()).hasSize(1);
        assertThat(testRecipe.getIngredients().iterator().next().getName()).isEqualTo(DEFAULT_INGREDIENT);
    }

    @Test
    @Transactional
    void createRecipeWithExistingId() throws Exception {
        // Create the Recipe with an existing ID
        recipe.setId(1L);
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        int databaseSizeBeforeCreate = recipeRepository.findAll(0,10).size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeMockMvc
                .perform(post(ENTITY_API_URL)
                        .header(API_KEY_HEADER, APIKey)
                        .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll(0,10);
        assertThat(recipeList).hasSize(databaseSizeBeforeCreate);
    }

    private void getWithCriteriaFound(String filter) throws Exception {
        restRecipeMockMvc
                .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter).header(API_KEY_HEADER, APIKey))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(recipe.getId().intValue())))
                .andExpect(jsonPath("$.[*].category").value(recipe.getCategory().toString()))
                .andExpect(jsonPath("$.[*].servings").value(hasItem(DEFAULT_SERVINGS)))
                .andExpect(jsonPath("$.[*].instructions").value(hasItem(DEFAULT_INSTRUCTIONS)))
                .andExpect(jsonPath("$.[*].ingredients[0].id").value(IngredientsSet.stream().toList().get(0).getId().intValue()))
                .andExpect(jsonPath("$.[*].ingredients[0].name").value(IngredientsSet.stream().toList().get(0).getName()));
    }

    private void getWithCriteriaNotFound(String filter) throws Exception {
        restRecipeMockMvc
                .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter).header(API_KEY_HEADER, APIKey))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Transactional
    void getNonExistingRecipe() throws Exception {
        // Get the recipe
        restRecipeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE).header(API_KEY_HEADER, APIKey)
        ).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void getRecipeWithoutApiKey() throws Exception {
        // Get the recipe
        restRecipeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void getRecipeWithInvalidApiKey() throws Exception {
        // Get the recipe
        restRecipeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE).header(API_KEY_HEADER, "wrong-api")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void putNonExistingRecipe() throws Exception {
        int databaseSizeBeforeUpdate = recipeRepository.findAll(0,10).size();
        recipe.setId(count.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, recipeDTO.id())
                                .header(API_KEY_HEADER, APIKey)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(recipeDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll(0,10);
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecipe() throws Exception {
        int databaseSizeBeforeUpdate = recipeRepository.findAll(0,10).size();
        recipe.setId(count.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, count.incrementAndGet())
                                .header(API_KEY_HEADER, APIKey)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(recipeDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll(0,10);
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecipe() throws Exception {
        int databaseSizeBeforeUpdate = recipeRepository.findAll(0,10).size();
        recipe.setId(count.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
                .perform(put(ENTITY_API_URL)
                        .header(API_KEY_HEADER, APIKey)
                        .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll(0,10);
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
    }
}
