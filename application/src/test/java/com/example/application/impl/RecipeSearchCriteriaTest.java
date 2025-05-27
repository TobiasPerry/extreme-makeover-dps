package com.example.application.impl;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeSearchCriteriaTest {

    @Test
    void testBuilder() {
        RecipeSearchCriteria criteria = new RecipeSearchCriteria(
                "Dessert",
                2,
                6,
                "mix thoroughly",
                List.of("Sugar", "Flour")
        );

        assertEquals("Dessert", criteria.category());
        assertEquals(2, criteria.minServings());
        assertEquals(6, criteria.maxServings());
        assertEquals("mix thoroughly", criteria.instructionsContains());
        assertEquals(List.of("Sugar", "Flour"), criteria.ingredientNames());
    }

    @Test
    void testIngredientNamesDefaultsToEmptyListIfNull() {
        RecipeSearchCriteria criteria = new RecipeSearchCriteria(
                "Main",
                0,
                0,
                "boil",
                null
        );

        assertNotNull(criteria.ingredientNames());
        assertTrue(criteria.ingredientNames().isEmpty());
    }
}
