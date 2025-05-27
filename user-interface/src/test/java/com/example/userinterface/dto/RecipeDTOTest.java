package com.example.userinterface.dto;

import com.example.domain.model.RecipeCategory;
import com.example.userinterface.dto.IngredientDTO;
import com.example.userinterface.dto.RecipeDTO;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        RecipeDTO recipeDTO1 = new RecipeDTO(1L, RecipeCategory.nonvegetarian.toString(), 7, "Cook for 20 minutes",
                new HashSet<IngredientDTO>(Collections.singleton(new IngredientDTO(1L, "meat"))));
        RecipeDTO recipeDTO2 = new RecipeDTO(2L, null, null, null, null);
        RecipeDTO recipeDTO3 = new RecipeDTO(1L, RecipeCategory.nonvegetarian.toString(), 7, "Cook for 20 minutes",
                new HashSet<IngredientDTO>(Collections.singleton(new IngredientDTO(1L, "meat"))));
        RecipeDTO recipeDTO4 = new RecipeDTO(null, null, null, null, null);
        assertThat(recipeDTO1).isNotEqualTo(recipeDTO2);
        assertThat(recipeDTO1).isNotEqualTo(recipeDTO4);
        assertThat(recipeDTO1).isEqualTo(recipeDTO3);
    }
}
