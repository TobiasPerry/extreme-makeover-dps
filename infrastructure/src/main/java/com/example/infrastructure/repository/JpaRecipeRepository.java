package com.example.infrastructure.repository;

import com.example.infrastructure.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaRecipeRepository extends JpaRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity> {
    Optional<RecipeEntity> findById(Long id);

    @Query("SELECT r FROM RecipeEntity r JOIN FETCH r.ingredients WHERE r.id IN :ids")
    List<RecipeEntity> findAllWithIngredientsByIds(@Param("ids") List<Long> ids);
}