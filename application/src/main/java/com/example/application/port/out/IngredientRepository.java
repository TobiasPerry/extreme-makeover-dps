package com.example.application.port.out;


import com.example.domain.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository {
    Optional<Ingredient> findById(Long id);
    List<Ingredient> findAll(int page, int size);
    Ingredient save(Ingredient ingredient);
    void deleteById(Long id);
    boolean existsById(Long id);
}