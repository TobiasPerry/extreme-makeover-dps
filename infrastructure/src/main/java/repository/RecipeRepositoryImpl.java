package repository;


import entity.RecipeEntity;
import domain.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import port.out.RecipeRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepositoryImpl extends JpaRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity>, RecipeRepository {
    @Override
    @Query("SELECT r FROM RecipeEntity r JOIN FETCH r.ingredients WHERE r.id IN :ids")
    List<Recipe> findAllWithIngredientsByIds(@Param("ids") List<Long> ids);

    @Override
    default Page<Recipe> findAll(Pageable pageable) {
        return findAll(pageable).map(RecipeEntity::toDomain);
    }

    @Override
    default Optional<Recipe> findById(Long id) {
        return findById(id).map(RecipeEntity::toDomain);
    }

    @Override
    default Recipe save(Recipe recipe) {
        RecipeEntity entity = RecipeEntity.fromDomain(recipe);
        return save(entity).toDomain();
    }
} 