package com.example.application.impl;

import com.example.application.port.out.RecipeRepository;
import com.example.domain.model.Recipe;
import com.example.domain.model.RecipeCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeQueryServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    private RecipeQueryServiceImpl recipeQueryService;

    @BeforeEach
    void setUp() {
        recipeQueryService = new RecipeQueryServiceImpl(recipeRepository);
    }

    @Test
    void findByCriteria_ShouldReturnMatchingRecipes() {
        // Arrange
        RecipeCriteria criteria = new RecipeCriteria();
        RecipeCriteria.RecipeCategoryFilter categoryFilter = new RecipeCriteria.RecipeCategoryFilter();
        categoryFilter.setEq(RecipeCategory.vegetarian);
        criteria.setCategory(categoryFilter);

        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setCategory(RecipeCategory.vegetarian);
        recipe1.setServings(4);
        recipe1.setInstructions("Test instructions 1");

        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setCategory(RecipeCategory.vegetarian);
        recipe2.setServings(6);
        recipe2.setInstructions("Test instructions 2");

        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        when(recipeRepository.findByCriteria(any(RecipeCriteria.class), eq(0), eq(10))).thenReturn(recipes);

        // Act
        List<Recipe> foundRecipes = recipeQueryService.findByCriteria(criteria, 0, 10);

        // Assert
        assertNotNull(foundRecipes);
        assertEquals(2, foundRecipes.size());
        assertEquals(RecipeCategory.vegetarian, foundRecipes.get(0).getCategory());
        assertEquals(RecipeCategory.vegetarian, foundRecipes.get(1).getCategory());
        verify(recipeRepository).findByCriteria(criteria, 0, 10);
    }

    @Test
    void findByCriteria_WithEmptyResult_ShouldReturnEmptyList() {
        // Arrange
        RecipeCriteria criteria = new RecipeCriteria();
        RecipeCriteria.RecipeCategoryFilter categoryFilter = new RecipeCriteria.RecipeCategoryFilter();
        categoryFilter.setEq(RecipeCategory.nonvegetarian);
        criteria.setCategory(categoryFilter);

        when(recipeRepository.findByCriteria(any(RecipeCriteria.class), eq(0), eq(10))).thenReturn(List.of());

        // Act
        List<Recipe> foundRecipes = recipeQueryService.findByCriteria(criteria, 0, 10);

        // Assert
        assertNotNull(foundRecipes);
        assertTrue(foundRecipes.isEmpty());
        verify(recipeRepository).findByCriteria(criteria, 0, 10);
    }
} 