package com.example.application.port.in;

import com.example.domain.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientService {

    Ingredient save(Ingredient ingredient);

    Ingredient update(Ingredient ingredient);

    List<Ingredient> findAll(int page, int size);

    Optional<Ingredient> findOne(Long id);

    void delete(Long id);
}
