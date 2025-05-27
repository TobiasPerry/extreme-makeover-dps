package com.example.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    private Ingredient ingredient;
    private Recipe recipe1;
    private Recipe recipe2;

    @BeforeEach
    void setUp() {
        ingredient = new Ingredient();
        recipe1 = new Recipe();
        recipe1.setId(1L);

        recipe2 = new Recipe();
        recipe2.setId(2L);
    }

    @Test
    void testSettersAndGetters() {
        ingredient.setId(10L);
        ingredient.setName("Salt");

        assertEquals(10L, ingredient.getId());
        assertEquals("Salt", ingredient.getName());
    }

    @Test
    void testAddRecipe() {
        ingredient.addRecipe(recipe1);
        assertTrue(ingredient.getRecipes().contains(recipe1));
    }

    @Test
    void testRemoveRecipe() {
        ingredient.addRecipe(recipe1);
        ingredient.removeRecipe(recipe1);
        assertFalse(ingredient.getRecipes().contains(recipe1));
    }

    @Test
    void testSetRecipes() {
        ingredient.setRecipes(Set.of(recipe1, recipe2));
        assertEquals(2, ingredient.getRecipes().size());
        assertTrue(ingredient.getRecipes().contains(recipe1));
        assertTrue(ingredient.getRecipes().contains(recipe2));
    }

    @Test
    void testEqualsAndHashCode() {
        Ingredient i1 = new Ingredient();
        i1.setId(100L);
        i1.setName("Changuche");

        Ingredient i2 = new Ingredient();
        i2.setId(100L);
        i2.setName("Changuche");

        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());
    }

    @Test
    void testToString() {
        ingredient.setId(99L);
        ingredient.setName("Olive Oil");

        String expected = "Ingredient{id=99, name='Olive Oil'}";
        assertEquals(expected, ingredient.toString());
    }
}
