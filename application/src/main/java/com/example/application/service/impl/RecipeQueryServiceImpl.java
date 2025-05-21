package com.example.application.service.impl;

import com.example.application.service.RecipeQueryService;
import com.example.application.service.RecipeSearchCriteria;
import com.example.domain.model.Recipe;
import com.example.domain.port.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RecipeQueryServiceImpl implements RecipeQueryService {

    private final RecipeRepository recipeRepository;

    public RecipeQueryServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Page<Recipe> findRecipes(RecipeSearchCriteria criteria, Pageable pageable) {
        return recipeRepository.findByCriteria(
            criteria.category(),
            criteria.minServings(),
            criteria.maxServings(),
            criteria.instructionsContains(),
            criteria.ingredientNames(),
            pageable
        );
    }
} 