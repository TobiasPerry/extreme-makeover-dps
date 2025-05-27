package com.example.application.impl;

import com.example.domain.model.RecipeCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeCriteriaTest {

    @Test
    void copy_ShouldCreateDeepCopy() {
        // Arrange
        RecipeCriteria original = new RecipeCriteria();
        
        // Set up ID filter
        Filter<Long> idFilter = new Filter<>();
        idFilter.setEq(1L);
        original.setId(idFilter);

        // Set up category filter
        RecipeCriteria.RecipeCategoryFilter categoryFilter = new RecipeCriteria.RecipeCategoryFilter();
        categoryFilter.setEq(RecipeCategory.vegetarian);
        original.setCategory(categoryFilter);

        // Set up servings filter
        Filter<Integer> servingsFilter = new Filter<>();
        servingsFilter.setEq(4);
        original.setServings(servingsFilter);

        // Set up ingredients filter
        Filter<String> ingredientsFilter = new Filter<>();
        ingredientsFilter.setContains("tomato");
        original.setIngredients(ingredientsFilter);

        // Set up instructions filter
        Filter<String> instructionsFilter = new Filter<>();
        instructionsFilter.setContains("bake");
        original.setInstructions(instructionsFilter);

        // Act
        RecipeCriteria copy = new RecipeCriteria(original);

        // Assert
        assertNotSame(original, copy);
        assertNotSame(original.getId(), copy.getId());
        assertNotSame(original.getCategory(), copy.getCategory());
        assertNotSame(original.getServings(), copy.getServings());
        assertNotSame(original.getIngredients(), copy.getIngredients());
        assertNotSame(original.getInstructions(), copy.getInstructions());

        assertEquals(original.getId().getEq(), copy.getId().getEq());
        assertEquals(original.getCategory().getEq(), copy.getCategory().getEq());
        assertEquals(original.getServings().getEq(), copy.getServings().getEq());
        assertEquals(original.getIngredients().getContains(), copy.getIngredients().getContains());
        assertEquals(original.getInstructions().getContains(), copy.getInstructions().getContains());
    }

    @Test
    void copy_WithNullFilters_ShouldCreateDeepCopy() {
        // Arrange
        RecipeCriteria original = new RecipeCriteria();

        // Act
        RecipeCriteria copy = new RecipeCriteria(original);

        // Assert
        assertNotSame(original, copy);
        assertNull(copy.getId());
        assertNull(copy.getCategory());
        assertNull(copy.getServings());
        assertNull(copy.getIngredients());
        assertNull(copy.getInstructions());
    }

    @Test
    void equals_ShouldCompareAllFields() {
        // Arrange
        RecipeCriteria criteria1 = new RecipeCriteria();
        RecipeCriteria criteria2 = new RecipeCriteria();

        // Set up ID filter
        Filter<Long> idFilter = new Filter<>();
        idFilter.setEq(1L);
        criteria1.setId(idFilter);
        criteria2.setId(idFilter.copy());

        // Set up category filter
        RecipeCriteria.RecipeCategoryFilter categoryFilter = new RecipeCriteria.RecipeCategoryFilter();
        categoryFilter.setEq(RecipeCategory.vegetarian);
        criteria1.setCategory(categoryFilter);
        criteria2.setCategory(categoryFilter.copy());

        // Set up servings filter
        Filter<Integer> servingsFilter = new Filter<>();
        servingsFilter.setEq(4);
        criteria1.setServings(servingsFilter);
        criteria2.setServings(servingsFilter.copy());

        // Set up ingredients filter
        Filter<String> ingredientsFilter = new Filter<>();
        ingredientsFilter.setContains("tomato");
        criteria1.setIngredients(ingredientsFilter);
        criteria2.setIngredients(ingredientsFilter.copy());

        // Set up instructions filter
        Filter<String> instructionsFilter = new Filter<>();
        instructionsFilter.setContains("bake");
        criteria1.setInstructions(instructionsFilter);
        criteria2.setInstructions(instructionsFilter.copy());

        // Act & Assert
        assertTrue(criteria1.equals(criteria2));
        assertTrue(criteria2.equals(criteria1));
        assertEquals(criteria1.hashCode(), criteria2.hashCode());
    }

    @Test
    void equals_WithDifferentValues_ShouldReturnFalse() {
        // Arrange
        RecipeCriteria criteria1 = new RecipeCriteria();
        RecipeCriteria criteria2 = new RecipeCriteria();

        // Set up different category filters
        RecipeCriteria.RecipeCategoryFilter categoryFilter1 = new RecipeCriteria.RecipeCategoryFilter();
        categoryFilter1.setEq(RecipeCategory.vegetarian);
        criteria1.setCategory(categoryFilter1);

        RecipeCriteria.RecipeCategoryFilter categoryFilter2 = new RecipeCriteria.RecipeCategoryFilter();
        categoryFilter2.setEq(RecipeCategory.nonvegetarian);
        criteria2.setCategory(categoryFilter2);

        // Act & Assert
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));
        assertNotEquals(criteria1.hashCode(), criteria2.hashCode());
    }

    @Test
    void toString_ShouldIncludeAllFields() {
        // Arrange
        RecipeCriteria criteria = new RecipeCriteria();
        
        // Set up ID filter
        Filter<Long> idFilter = new Filter<>();
        idFilter.setEq(1L);
        criteria.setId(idFilter);

        // Set up category filter
        RecipeCriteria.RecipeCategoryFilter categoryFilter = new RecipeCriteria.RecipeCategoryFilter();
        categoryFilter.setEq(RecipeCategory.vegetarian);
        criteria.setCategory(categoryFilter);

        // Act
        String toString = criteria.toString();

        // Assert
        assertTrue(toString.contains("id="));
        assertTrue(toString.contains("category="));
        assertTrue(toString.contains("servings="));
        assertTrue(toString.contains("ingredients="));
        assertTrue(toString.contains("instructions="));
    }
} 