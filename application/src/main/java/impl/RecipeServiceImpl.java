package impl;



import domain.model.Recipe;
import org.springframework.stereotype.Service;
import port.in.RecipeService;
import port.out.RecipeRepository;

import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe update(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Optional<Recipe> findOne(Long id) {
        return recipeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        recipeRepository.deleteById(id);
    }
}
