package com.example.application.port.in;

import com.example.domain.model.Recipe;

import java.util.Optional;

public interface RecipeService {
    Recipe save(Recipe recipe);
    Recipe update(Recipe recipe);
    Optional<Recipe> findOne(Long id);
    void delete(Long id);
}