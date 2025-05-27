package com.example.application.impl;

import com.example.application.port.out.RecipeRepository;
import com.example.domain.model.Recipe;
import com.example.domain.model.RecipeCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeServiceImpl(recipeRepository);
    }

    @Test
    void save_ShouldSaveAndReturnRecipe() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setCategory(RecipeCategory.vegetarian);
        recipe.setServings(4);
        recipe.setInstructions("Test instructions");

        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        // Act
        Recipe savedRecipe = recipeService.save(recipe);

        // Assert
        assertNotNull(savedRecipe);
        assertEquals(RecipeCategory.vegetarian, savedRecipe.getCategory());
        assertEquals(4, savedRecipe.getServings());
        assertEquals("Test instructions", savedRecipe.getInstructions());
        verify(recipeRepository).save(recipe);
    }

    @Test
    void update_ShouldUpdateAndReturnRecipe() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setCategory(RecipeCategory.nonvegetarian);
        recipe.setServings(6);
        recipe.setInstructions("Updated instructions");

        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        // Act
        Recipe updatedRecipe = recipeService.update(recipe);

        // Assert
        assertNotNull(updatedRecipe);
        assertEquals(1L, updatedRecipe.getId());
        assertEquals(RecipeCategory.nonvegetarian, updatedRecipe.getCategory());
        assertEquals(6, updatedRecipe.getServings());
        assertEquals("Updated instructions", updatedRecipe.getInstructions());
        verify(recipeRepository).save(recipe);
    }

    @Test
    void findOne_WhenRecipeExists_ShouldReturnRecipe() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setCategory(RecipeCategory.vegetarian);
        recipe.setServings(4);
        recipe.setInstructions("Test instructions");

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        // Act
        Optional<Recipe> foundRecipe = recipeService.findOne(1L);

        // Assert
        assertTrue(foundRecipe.isPresent());
        assertEquals(1L, foundRecipe.get().getId());
        assertEquals(RecipeCategory.vegetarian, foundRecipe.get().getCategory());
        assertEquals(4, foundRecipe.get().getServings());
        assertEquals("Test instructions", foundRecipe.get().getInstructions());
        verify(recipeRepository).findById(1L);
    }

    @Test
    void findOne_WhenRecipeDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Recipe> foundRecipe = recipeService.findOne(1L);

        // Assert
        assertTrue(foundRecipe.isEmpty());
        verify(recipeRepository).findById(1L);
    }

    @Test
    void delete_ShouldDeleteRecipe() {
        // Arrange
        Long recipeId = 1L;

        // Act
        recipeService.delete(recipeId);

        // Assert
        verify(recipeRepository).deleteById(recipeId);
    }
} 