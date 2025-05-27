package com.example.userinterface.dto;


import jakarta.validation.constraints.NotBlank;

/**
 * A DTO for the {@link com.example.recipeapi.domain.Ingredient} entity.
 */
public record IngredientDTO(Long id,
                            @NotBlank(message = "Name is required") String name) {
}
