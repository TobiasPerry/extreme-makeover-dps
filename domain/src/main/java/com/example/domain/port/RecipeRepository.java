package com.example.domain.port;

import com.example.domain.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeRepository {
    /**
     * Find recipes by various criteria
     *
     * @param category recipe category
     * @param minServings minimum number of servings
     * @param maxServings maximum number of servings
     * @param instructionsContains text to search in instructions
     * @param ingredientNames list of ingredient names to search for
     * @param pageable pagination information
     * @return page of recipes matching the criteria
     */
    Page<Recipe> findByCriteria(
        String category,
        Integer minServings,
        Integer maxServings,
        String instructionsContains,
        List<String> ingredientNames,
        Pageable pageable
    );

    /**
     * Find a recipe by its ID
     *
     * @param id recipe ID
     * @return the recipe if found
     */
    Recipe findById(Long id);

    /**
     * Save a recipe
     *
     * @param recipe recipe to save
     * @return the saved recipe
     */
    Recipe save(Recipe recipe);

    /**
     * Delete a recipe by its ID
     *
     * @param id recipe ID
     */
    void deleteById(Long id);
} 