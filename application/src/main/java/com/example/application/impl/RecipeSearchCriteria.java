package com.example.application.impl;

import java.util.List;
import java.util.Objects;

public record RecipeSearchCriteria(
    String category,
    Integer minServings,
    Integer maxServings,
    String instructionsContains,
    List<String> ingredientNames
) {
    public RecipeSearchCriteria {
        ingredientNames = List.copyOf(Objects.requireNonNullElse(ingredientNames, List.of()));
    }
} 