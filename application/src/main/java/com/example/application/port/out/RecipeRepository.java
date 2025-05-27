package com.example.application.port.out;

import com.example.domain.model.Recipe;
import com.example.application.impl.RecipeCriteria;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository {
    Optional<Recipe> findById(Long id);
    List<Recipe> findAll(int page, int size);
    List<Recipe> findAllWithIngredientsByIds(List<Long> ids);
    Recipe save(Recipe recipe);
    void deleteById(Long id);
    boolean existsById(Long id);

    List<Recipe> findByCriteria(RecipeCriteria criteria, int page, int size);
}

