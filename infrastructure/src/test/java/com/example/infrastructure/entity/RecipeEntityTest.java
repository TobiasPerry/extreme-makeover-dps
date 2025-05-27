package com.example.infrastructure.entity;

import com.example.domain.model.Recipe;
import com.example.domain.model.RecipeCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeEntityTest {

    private RecipeEntity recipeEntity;
    private IngredientEntity ingredientEntity;

    @BeforeEach
    void setUp() {
        recipeEntity = new RecipeEntity();
        recipeEntity.setId(1L);
        recipeEntity.setCategory(RecipeCategory.vegetarian);
        recipeEntity.setServings(4);
        recipeEntity.setInstructions("Test instructions");
        recipeEntity.setIngredients(new HashSet<>());

        ingredientEntity = new IngredientEntity();
        ingredientEntity.setId(1L);
        ingredientEntity.setName("Test Ingredient");
        ingredientEntity.setRecipes(new HashSet<>());
    }

    @Test
    void testGettersAndSetters() {
        // Test id
        assertEquals(1L, recipeEntity.getId());
        recipeEntity.setId(2L);
        assertEquals(2L, recipeEntity.getId());

        // Test category
        assertEquals(RecipeCategory.vegetarian, recipeEntity.getCategory());
        recipeEntity.setCategory(RecipeCategory.nonvegetarian);
        assertEquals(RecipeCategory.nonvegetarian, recipeEntity.getCategory());

        // Test servings
        assertEquals(4, recipeEntity.getServings());
        recipeEntity.setServings(6);
        assertEquals(6, recipeEntity.getServings());

        // Test instructions
        assertEquals("Test instructions", recipeEntity.getInstructions());
        recipeEntity.setInstructions("New instructions");
        assertEquals("New instructions", recipeEntity.getInstructions());

        // Test ingredients
        Set<IngredientEntity> ingredients = new HashSet<>();
        ingredients.add(ingredientEntity);
        recipeEntity.setIngredients(ingredients);
        assertEquals(1, recipeEntity.getIngredients().size());
        assertTrue(recipeEntity.getIngredients().contains(ingredientEntity));
    }

    @Test
    void testAddIngredient() {
        recipeEntity.addIngredient(ingredientEntity);
        assertTrue(recipeEntity.getIngredients().contains(ingredientEntity));
        assertTrue(ingredientEntity.getRecipes().contains(recipeEntity));
    }

    @Test
    void testRemoveIngredient() {
        recipeEntity.addIngredient(ingredientEntity);
        recipeEntity.removeIngredient(ingredientEntity);
        assertFalse(recipeEntity.getIngredients().contains(ingredientEntity));
        assertFalse(ingredientEntity.getRecipes().contains(recipeEntity));
    }

    @Test
    void testEqualsAndHashCode() {
        RecipeEntity entity1 = new RecipeEntity();
        entity1.setId(1L);
        entity1.setCategory(RecipeCategory.vegetarian);

        RecipeEntity entity2 = new RecipeEntity();
        entity2.setId(1L);
        entity2.setCategory(RecipeCategory.nonvegetarian);

        RecipeEntity entity3 = new RecipeEntity();
        entity3.setId(2L);
        entity3.setCategory(RecipeCategory.vegetarian);

        // Test equals
        assertTrue(entity1.equals(entity1));
        assertTrue(entity1.equals(entity2));
        assertFalse(entity1.equals(entity3));
        assertFalse(entity1.equals(null));
        assertFalse(entity1.equals(new Object()));

        // Test hashCode
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    @Test
    void testToString() {
        String expected = "RecipeEntity{id=1, category='vegetarian', servings=4, instructions='Test instructions'}";
        assertEquals(expected, recipeEntity.toString());
    }

    @Test
    void testToDomain() {
        Recipe domain = recipeEntity.toDomain();
        assertEquals(recipeEntity.getId(), domain.getId());
        assertEquals(recipeEntity.getCategory(), domain.getCategory());
        assertEquals(recipeEntity.getServings(), domain.getServings());
        assertEquals(recipeEntity.getInstructions(), domain.getInstructions());
    }

    @Test
    void testFromDomain() {
        Recipe domain = new Recipe();
        domain.setId(1L);
        domain.setCategory(RecipeCategory.vegetarian);
        domain.setServings(4);
        domain.setInstructions("Test instructions");

        RecipeEntity entity = RecipeEntity.fromDomain(domain);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getCategory(), entity.getCategory());
        assertEquals(domain.getServings(), entity.getServings());
        assertEquals(domain.getInstructions(), entity.getInstructions());
    }
} 