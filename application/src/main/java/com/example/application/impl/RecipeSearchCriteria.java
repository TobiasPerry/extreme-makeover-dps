package com.example.application.impl;

import java.util.List;

public record RecipeSearchCriteria(
    String category,
    Integer minServings,
    Integer maxServings,
    String instructionsContains,
    List<String> ingredientNames
) {
    public RecipeSearchCriteria {
        if (ingredientNames == null) {
            ingredientNames = List.of();
        }
    }
} 