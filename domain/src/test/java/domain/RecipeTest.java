package domain;

import com.example.domain.model.Ingredient;
import com.example.domain.model.Recipe;
import com.example.domain.model.RecipeCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    private Recipe recipe;
    private Ingredient ingredient1;
    private Ingredient ingredient2;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
        ingredient1 = new Ingredient();
        ingredient1.setName("Sugar");
        ingredient1.setId(1L);

        ingredient2 = new Ingredient();
        ingredient2.setName("Flour");
        ingredient1.setId(2L);

    }

    @Test
    void testSettersAndGetters() {
        RecipeCategory category = RecipeCategory.vegetarian;
        recipe.setId(1L);
        recipe.setCategory(category);
        recipe.setServings(4);
        recipe.setInstructions("Mix ingredients and bake.");

        assertEquals(1L, recipe.getId());
        assertEquals(category, recipe.getCategory());
        assertEquals(4, recipe.getServings());
        assertEquals("Mix ingredients and bake.", recipe.getInstructions());
    }

    @Test
    void testAddIngredient() {
        recipe.addIngredient(ingredient1);
        assertTrue(recipe.getIngredients().contains(ingredient1));
    }

    @Test
    void testRemoveIngredient() {
        recipe.addIngredient(ingredient1);
        recipe.removeIngredient(ingredient1);
        assertFalse(recipe.getIngredients().contains(ingredient1));
    }

    @Test
    void testSetIngredients() {
        recipe.setIngredients(Set.of(ingredient1, ingredient2));
        assertEquals(2, recipe.getIngredients().size());
        assertTrue(recipe.getIngredients().contains(ingredient1));
        assertTrue(recipe.getIngredients().contains(ingredient2));
    }

    @Test
    void testEqualsAndHashCode() {
        Recipe recipe1 = new Recipe();
        recipe1.setId(100L);

        Recipe recipe2 = new Recipe();
        recipe2.setId(100L);

        assertEquals(recipe1, recipe2);
        assertEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testToString() {
        recipe.setId(10L);
        recipe.setCategory(RecipeCategory.vegetarian);
        recipe.setServings(2);
        recipe.setInstructions("Boil water");

        String expected = "Recipe{id=10, category='vegetarian', servings=2, instructions='Boil water'}";
        assertEquals(expected, recipe.toString());
    }
}
