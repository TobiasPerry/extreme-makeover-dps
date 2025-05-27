package com.example.application.impl;


import com.example.application.port.in.RecipeQueryService;
import com.example.application.port.out.RecipeRepository;
import com.example.domain.model.Recipe;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeQueryServiceImpl implements RecipeQueryService {

    private final RecipeRepository recipeRepository;

    public RecipeQueryServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> findByCriteria(RecipeCriteria criteria, int page, int size) {
        return recipeRepository.findByCriteria(criteria,page,size);
    }
} 