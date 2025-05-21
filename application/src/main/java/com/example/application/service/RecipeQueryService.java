package com.example.application.service;

import com.example.domain.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeQueryService {
    /**
     * Find recipes based on search criteria
     *
     * @param searchCriteria the search criteria
     * @param pageable pagination information
     * @return page of recipes matching the criteria
     */
    Page<Recipe> findRecipes(RecipeSearchCriteria searchCriteria, Pageable pageable);
} 