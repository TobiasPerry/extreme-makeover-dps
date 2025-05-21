package com.example.userinterface.dto;

import jakarta.validation.constraints.NotBlank;

public record IngredientDTO(
    Long id,
    @NotBlank(message = "Name is required") 
    String name
) {} 