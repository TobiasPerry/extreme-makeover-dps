package repository;


import entity.IngredientEntity;
import domain.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.out.IngredientRepository;

import java.util.Optional;

@Repository
public interface IngredientRepositoryImpl extends JpaRepository<IngredientEntity, Long>, IngredientRepository {
    @Override
    default Optional<Ingredient> findById(Long id) {
        return findById(id).map(IngredientEntity::toDomain);
    }

    @Override
    default Ingredient save(Ingredient ingredient) {
        IngredientEntity entity = IngredientEntity.fromDomain(ingredient);
        return save(entity).toDomain();
    }
} 