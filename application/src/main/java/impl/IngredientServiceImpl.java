package impl;
import domain.model.Ingredient;
import port.in.IngredientService;
import port.out.IngredientRepository;

import java.util.List;
import java.util.Optional;

public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @Override
    public Ingredient update(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @Override
    public List<Ingredient> findAll(int page, int size){
        return ingredientRepository.findAll(page, size);
    }

    @Override
    public Optional<Ingredient> findOne(Long id) {
        return ingredientRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        ingredientRepository.deleteById(id);
    }
}
