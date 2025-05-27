package com.example.userinterface.rest;

import com.example.domain.model.Recipe;
import com.example.domain.model.Ingredient;
import com.example.domain.model.RecipeCategory;
import com.example.userinterface.dto.RecipeDTO;
import com.example.userinterface.dto.IngredientDTO;
import com.example.userinterface.mapper.RecipeMapper;
import com.example.application.port.in.RecipeService;
import com.example.application.port.in.RecipeQueryService;
import com.example.application.port.out.RecipeRepository;
import com.example.userinterface.rest.exceptions.BadRequestException;
import com.example.application.impl.RecipeCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RecipeResourceTest {

    @Mock
    private RecipeService recipeService;

    @Mock
    private RecipeQueryService recipeQueryService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeResource recipeResource;

    private RecipeDTO recipeDTO;
    private Recipe recipe;
    private Set<Ingredient> ingredients;
    private Set<IngredientDTO> ingredientDTOs;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test data
        ingredients = new HashSet<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Test Ingredient");
        ingredients.add(ingredient);

        ingredientDTOs = new HashSet<>();
        ingredientDTOs.add(new IngredientDTO(1L, "Test Ingredient"));

        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setCategory(RecipeCategory.vegetarian);
        recipe.setServings(4);
        recipe.setInstructions("Test Instructions");
        recipe.setIngredients(ingredients);

        recipeDTO = new RecipeDTO(1L, RecipeCategory.vegetarian.name(), 4, "Test Instructions", ingredientDTOs);
    }

    @Test
    void createRecipe_Success() throws URISyntaxException {
        // Arrange
        RecipeDTO newRecipeDTO = new RecipeDTO(null, RecipeCategory.vegetarian.name(), 4, "New Instructions", ingredientDTOs);
        Recipe newRecipe = new Recipe();
        newRecipe.setCategory(RecipeCategory.vegetarian);
        newRecipe.setServings(4);
        newRecipe.setInstructions("New Instructions");
        newRecipe.setIngredients(ingredients);

        when(recipeMapper.toEntity(newRecipeDTO)).thenReturn(newRecipe);
        when(recipeService.save(any(Recipe.class))).thenReturn(newRecipe);
        when(recipeMapper.toDto(newRecipe)).thenReturn(newRecipeDTO);

        // Act
        ResponseEntity<RecipeDTO> response = recipeResource.createRecipe(newRecipeDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(RecipeCategory.vegetarian.name(), response.getBody().category());
        verify(recipeService).save(any(Recipe.class));
    }

    @Test
    void createRecipe_WithExistingId_ThrowsException() {
        // Arrange
        RecipeDTO existingRecipeDTO = new RecipeDTO(1L, RecipeCategory.vegetarian.name(), 4, "Existing Instructions", ingredientDTOs);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> recipeResource.createRecipe(existingRecipeDTO));
        verify(recipeService, never()).save(any(Recipe.class));
    }

    @Test
    void updateRecipe_Success() throws URISyntaxException {
        // Arrange
        when(recipeRepository.existsById(1L)).thenReturn(true);
        when(recipeMapper.toEntity(recipeDTO)).thenReturn(recipe);
        when(recipeService.update(any(Recipe.class))).thenReturn(recipe);
        when(recipeMapper.toDto(recipe)).thenReturn(recipeDTO);

        // Act
        ResponseEntity<RecipeDTO> response = recipeResource.updateRecipe(1L, recipeDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        verify(recipeService).update(any(Recipe.class));
    }

    @Test
    void updateRecipe_NonExistentId_ThrowsException() {
        // Arrange
        when(recipeRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> recipeResource.updateRecipe(1L, recipeDTO));
        verify(recipeService, never()).update(any(Recipe.class));
    }

    @Test
    void getRecipesByCriteria_Success() {
        // Arrange
        RecipeCriteria criteria = new RecipeCriteria();
        List<Recipe> recipes = Arrays.asList(recipe);
        when(recipeQueryService.findByCriteria(any(RecipeCriteria.class), anyInt(), anyInt())).thenReturn(recipes);
        when(recipeMapper.toDto(any(Recipe.class))).thenReturn(recipeDTO);

        // Act
        ResponseEntity<List<RecipeDTO>> response = recipeResource.getRecipesByCriteria(criteria, 10, 0);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(recipeQueryService).findByCriteria(any(RecipeCriteria.class), anyInt(), anyInt());
    }

    @Test
    void getRecipe_Success() {
        // Arrange
        when(recipeService.findOne(1L)).thenReturn(Optional.of(recipe));
        when(recipeMapper.toDto(recipe)).thenReturn(recipeDTO);

        // Act
        ResponseEntity<RecipeDTO> response = recipeResource.getRecipe(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        verify(recipeService).findOne(1L);
    }

    @Test
    void getRecipe_NotFound() {
        // Arrange
        when(recipeService.findOne(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<RecipeDTO> response = recipeResource.getRecipe(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(recipeService).findOne(1L);
    }

    @Test
    void deleteRecipe_Success() {
        // Act
        ResponseEntity<Void> response = recipeResource.deleteRecipe(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(recipeService).delete(1L);
    }
} 