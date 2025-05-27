package impl;


import domain.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import port.in.RecipeQueryService;
import port.out.RecipeRepository;

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