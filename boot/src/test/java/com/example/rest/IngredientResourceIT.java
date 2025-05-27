package com.example.rest;

import com.example.IntegrationTest;
import com.example.config.TestSecurityConfig;
import com.example.domain.model.Ingredient;
import com.example.userinterface.dto.IngredientDTO;
import jakarta.persistence.EntityManager;
import com.example.userinterface.mapper.IngredientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.example.application.port.out.IngredientRepository;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link IngredientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class IngredientResourceIT {

    private static final String API_KEY_HEADER = "x-api-key";
    private static final String APIKey = TestSecurityConfig.TEST_API_KEY;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ingredients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt());

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private IngredientMapper ingredientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngredientMockMvc;

    private Ingredient ingredient;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingredient createEntity(EntityManager em) {
        Ingredient i= new Ingredient();
        i.setName(DEFAULT_NAME);
        return i;
    }

    @BeforeEach
    public void initTest() {
        ingredient = createEntity(em);
    }

    @Test
    @Transactional
    void createIngredient() throws Exception {
        int databaseSizeBeforeCreate = ingredientRepository.findAll(0,10).size();
        // Create the Ingredient
        IngredientDTO ingredientDTO = ingredientMapper.toDto(ingredient);
        restIngredientMockMvc
                .perform(post(ENTITY_API_URL)
                        .header(API_KEY_HEADER, APIKey)
                        .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientDTO)))
                .andExpect(status().isCreated());

        // Validate the Ingredient in the database
        List<Ingredient> ingredientList = ingredientRepository.findAll(0,10);
        assertThat(ingredientList).hasSize(databaseSizeBeforeCreate + 1);
        Ingredient testIngredient = ingredientList.get(ingredientList.size() - 1);
        assertThat(testIngredient.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createIngredientWithExistingId() throws Exception {
        // Create the Ingredient with an existing ID
        ingredient.setId(1L);
        IngredientDTO ingredientDTO = ingredientMapper.toDto(ingredient);

        int databaseSizeBeforeCreate = ingredientRepository.findAll(0,10).size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngredientMockMvc
                .perform(post(ENTITY_API_URL)
                        .header(API_KEY_HEADER, APIKey)
                        .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Ingredient in the database
        List<Ingredient> ingredientList = ingredientRepository.findAll(0,10);
        assertThat(ingredientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientRepository.findAll(0,10).size();
        // set the field null
        ingredient.setName(null);

        // Create the Ingredient, which fails.
        IngredientDTO ingredientDTO = ingredientMapper.toDto(ingredient);

        restIngredientMockMvc
                .perform(post(ENTITY_API_URL)
                        .header(API_KEY_HEADER, APIKey)
                        .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientDTO)))
                .andExpect(status().isBadRequest());

        List<Ingredient> ingredientList = ingredientRepository.findAll(0,10);
        assertThat(ingredientList).hasSize(databaseSizeBeforeTest);
    }

//    @Test
//    @Transactional
//    void getAllIngredients() throws Exception {
//        // Initialize the database
//        ingredientRepository.save(ingredient);
//
//        // Get all the ingredientList
//        restIngredientMockMvc
//                .perform(get(ENTITY_API_URL + "?sort=id,desc").header(API_KEY_HEADER, APIKey))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.[*].id").value(hasItem(ingredient.getId().intValue())))
//                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
//    }
//
//    @Test
//    @Transactional
//    void getIngredient() throws Exception {
//        // Initialize the database
//        ingredientRepository.save(ingredient);
//
//        // Get the ingredient
//        restIngredientMockMvc
//                .perform(get(ENTITY_API_URL_ID, ingredient.getId()).header(API_KEY_HEADER, APIKey))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.id").value(ingredient.getId().intValue()))
//                .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
//    }

    @Test
    @Transactional
    void getNonExistingIngredient() throws Exception {
        // Get the ingredient
        restIngredientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE).header(API_KEY_HEADER, APIKey)).andExpect(status().isNotFound());
    }

//    @Test
//    @Transactional
//    void putExistingIngredient() throws Exception {
//        // Initialize the database
//        ingredientRepository.save(ingredient);
//
//        int databaseSizeBeforeUpdate = ingredientRepository.findAll(0,10).size();
//
//        // Update the ingredient
//        Ingredient updatedIngredient = ingredientRepository.findById(ingredient.getId()).get();
//        // Disconnect from session so that the updates on updatedIngredient are not directly saved in db
//        em.detach(updatedIngredient);
//        updatedIngredient.setName(UPDATED_NAME);
//        IngredientDTO ingredientDTO = ingredientMapper.toDto(updatedIngredient);
//
//        restIngredientMockMvc
//                .perform(
//                        put(ENTITY_API_URL_ID, ingredientDTO.id())
//                                .header(API_KEY_HEADER, APIKey)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(TestUtil.convertObjectToJsonBytes(ingredientDTO))
//                )
//                .andExpect(status().isOk());
//
//        // Validate the Ingredient in the database
//        List<Ingredient> ingredientList = ingredientRepository.findAll(0,10);
//        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
//        Ingredient testIngredient = ingredientList.get(ingredientList.size() - 1);
//        assertThat(testIngredient.getName()).isEqualTo(UPDATED_NAME);
//    }

    @Test
    @Transactional
    void putNonExistingIngredient() throws Exception {
        int databaseSizeBeforeUpdate = ingredientRepository.findAll(0,10).size();
        ingredient.setId(count.incrementAndGet());

        // Create the Ingredient
        IngredientDTO ingredientDTO = ingredientMapper.toDto(ingredient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, ingredientDTO.id())
                                .header(API_KEY_HEADER, APIKey)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(ingredientDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the Ingredient in the database
        List<Ingredient> ingredientList = ingredientRepository.findAll(0,10);
        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngredient() throws Exception {
        int databaseSizeBeforeUpdate = ingredientRepository.findAll(0,10).size();
        ingredient.setId(count.incrementAndGet());

        // Create the Ingredient
        IngredientDTO ingredientDTO = ingredientMapper.toDto(ingredient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, count.incrementAndGet())
                                .header(API_KEY_HEADER, APIKey)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(ingredientDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the Ingredient in the database
        List<Ingredient> ingredientList = ingredientRepository.findAll(0,10);
        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngredient() throws Exception {
        int databaseSizeBeforeUpdate = ingredientRepository.findAll(0,10).size();
        ingredient.setId(count.incrementAndGet());

        // Create the Ingredient
        IngredientDTO ingredientDTO = ingredientMapper.toDto(ingredient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientMockMvc
                .perform(put(ENTITY_API_URL)
                        .header(API_KEY_HEADER, APIKey)
                        .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientDTO)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Ingredient in the database
        List<Ingredient> ingredientList = ingredientRepository.findAll(0,10);
        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
    }

//    @Test
//    @Transactional
//    void deleteIngredient() throws Exception {
//        // Initialize the database
//        ingredientRepository.save(ingredient);
//
//        int databaseSizeBeforeDelete = ingredientRepository.findAll(0,10).size();
//
//        // Delete the ingredient
//        restIngredientMockMvc
//                .perform(delete(ENTITY_API_URL_ID, ingredient.getId()).header(API_KEY_HEADER, APIKey).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<Ingredient> ingredientList = ingredientRepository.findAll(0,10);
//        assertThat(ingredientList).hasSize(databaseSizeBeforeDelete - 1);
//    }
}
