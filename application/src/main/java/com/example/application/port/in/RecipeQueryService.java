package com.example.application.port.in;


import com.example.application.impl.RecipeCriteria;
import com.example.domain.model.Recipe;

import java.util.List;


public interface RecipeQueryService {
    /**
     * Find recipes based on structured search criteria.
     */
   List<Recipe> findByCriteria(RecipeCriteria searchCriteria, int page, int size);
}