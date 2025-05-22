package repository;



import domain.model.Ingredient;
import entity.IngredientEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import port.out.IngredientRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class IngredientRepositoryAdapter implements IngredientRepository {

    private final JpaIngredientRepository jpaIngredientRepository;

    public IngredientRepositoryAdapter(JpaIngredientRepository jpaIngredientRepository) {
        this.jpaIngredientRepository = jpaIngredientRepository;
    }

    @Override
    public Optional<Ingredient> findById(Long id) {
        return jpaIngredientRepository.findById(id)
                .map(IngredientEntity::toDomain);
    }

    @Override
    public List<Ingredient> findAll(int page,int size) {
        return jpaIngredientRepository.findAll(Pageable.ofSize(size).withPage(page))
                .stream()
                .map(IngredientEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        IngredientEntity saved = jpaIngredientRepository.save(IngredientEntity.fromDomain(ingredient));
        return saved.toDomain();
    }

    @Override
    public void deleteById(Long id) {
        jpaIngredientRepository.deleteById(id);
    }
}

