package com.example.infrastructure.entity;

import com.example.infrastructure.entity.IngredientEntity;
import com.example.domain.model.Ingredient;
import com.example.infrastructure.entity.RecipeEntity;
import com.example.domain.model.RecipeCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class IngredientEntityTest {

    private IngredientEntity ingredientEntity;
    private RecipeEntity recipeEntity;

    @BeforeEach
    void setUp() {
        ingredientEntity = new IngredientEntity();
        ingredientEntity.setId(1L);
        ingredientEntity.setName("Test Ingredient");
        ingredientEntity.setRecipes(new HashSet<>());

        recipeEntity = new RecipeEntity();
        recipeEntity.setId(1L);
        recipeEntity.setCategory(RecipeCategory.vegetarian);
        recipeEntity.setServings(4);
        recipeEntity.setInstructions("Test instructions");
        recipeEntity.setIngredients(new HashSet<>());
    }

    @Test
    void testGettersAndSetters() {
        // Test id
        assertEquals(1L, ingredientEntity.getId());
        ingredientEntity.setId(2L);
        assertEquals(2L, ingredientEntity.getId());

        // Test name
        assertEquals("Test Ingredient", ingredientEntity.getName());
        ingredientEntity.setName("New Name");
        assertEquals("New Name", ingredientEntity.getName());

        // Test recipes
        Set<RecipeEntity> recipes = new HashSet<>();
        recipes.add(recipeEntity);
        ingredientEntity.setRecipes(recipes);
        assertEquals(1, ingredientEntity.getRecipes().size());
        assertTrue(ingredientEntity.getRecipes().contains(recipeEntity));
    }

    @Test
    void testAddRecipe() {
        ingredientEntity.addRecipe(recipeEntity);
        assertTrue(ingredientEntity.getRecipes().contains(recipeEntity));
        assertTrue(recipeEntity.getIngredients().contains(ingredientEntity));
    }

    @Test
    void testRemoveRecipe() {
        ingredientEntity.addRecipe(recipeEntity);
        ingredientEntity.removeRecipe(recipeEntity);
        assertFalse(ingredientEntity.getRecipes().contains(recipeEntity));
        assertFalse(recipeEntity.getIngredients().contains(ingredientEntity));
    }

    @Test
    void testEqualsAndHashCode() {
        IngredientEntity entity1 = new IngredientEntity();
        entity1.setId(1L);
        entity1.setName("Test");

        IngredientEntity entity2 = new IngredientEntity();
        entity2.setId(1L);
        entity2.setName("Different");

        IngredientEntity entity3 = new IngredientEntity();
        entity3.setId(2L);
        entity3.setName("Test");

        // Test equals
        assertEquals(entity1, entity1);
        assertEquals(entity1, entity2);
        assertNotEquals(entity1, entity3);
        assertNotEquals(null, entity1);
        assertNotEquals(entity1, new Object());

        // Test hashCode
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    @Test
    void testToString() {
        String expected = "IngredientEntity{id=1, name='Test Ingredient'}";
        assertEquals(expected, ingredientEntity.toString());
    }

    @Test
    void testToDomain() {
        Ingredient domain = ingredientEntity.toDomain();
        assertEquals(ingredientEntity.getId(), domain.getId());
        assertEquals(ingredientEntity.getName(), domain.getName());
    }

    @Test
    void testFromDomain() {
        Ingredient domain = new Ingredient();
        domain.setId(1L);
        domain.setName("Test Ingredient");

        IngredientEntity entity = IngredientEntity.fromDomain(domain);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getName(), entity.getName());
    }
} 