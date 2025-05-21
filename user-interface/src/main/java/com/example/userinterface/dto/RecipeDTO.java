package com.example.userinterface.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

public record RecipeDTO(
    Long id,
    String category,
    @Min(value = 1, message = "servings must be a positive integer")
    Integer servings,
    @NotBlank(message = "instructions cannot be blank")
    String instructions,
    Set<IngredientDTO> ingredients) {

    public RecipeDTO {
        if (ingredients == null) {
            ingredients = new HashSet<>();
        }
    }
} 