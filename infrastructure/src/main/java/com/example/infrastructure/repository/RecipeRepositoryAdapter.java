package com.example.infrastructure.repository;


import com.example.infrastructure.entity.RecipeEntity;
import com.example.infrastructure.entity.IngredientEntity_;
import com.example.domain.model.Recipe;
import com.example.infrastructure.entity.RecipeEntity_;
import com.example.application.impl.RecipeCriteria;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import com.example.application.port.out.RecipeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RecipeRepositoryAdapter implements RecipeRepository {

    private final JpaRecipeRepository jpaRecipeRepository;
    private final QueryBuilderService<RecipeEntity> queryBuilder;

    public RecipeRepositoryAdapter(
            JpaRecipeRepository jpaRecipeRepository,
            QueryBuilderService<RecipeEntity> queryBuilder
    ) {
        this.jpaRecipeRepository = jpaRecipeRepository;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return jpaRecipeRepository.findById(id)
                .map(RecipeEntity::toDomain);
    }

    @Override
    public List<Recipe> findAll(int page, int size) {
        return jpaRecipeRepository.findAll(PageRequest.of(page, size))
                .map(RecipeEntity::toDomain)
                .toList();
    }

    @Override
    public List<Recipe> findAllWithIngredientsByIds(List<Long> ids) {
        return jpaRecipeRepository.findAllWithIngredientsByIds(ids)
                .stream()
                .map(RecipeEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Recipe save(Recipe recipe) {
        RecipeEntity saved = jpaRecipeRepository.save(RecipeEntity.fromDomain(recipe));
        return saved.toDomain();
    }

    @Override
    public void deleteById(Long id) {
        jpaRecipeRepository.deleteById(id);
    }

    @Override
    public List<Recipe> findByCriteria(RecipeCriteria criteria, int page, int size) {
        Specification<RecipeEntity> specification = buildSpecification(criteria);
        Page<RecipeEntity> resultPage = jpaRecipeRepository.findAll(specification, PageRequest.of(page, size));
        return resultPage.getContent().stream()
                .map(RecipeEntity::toDomain)
                .collect(Collectors.toList());
    }

    private Specification<RecipeEntity> buildSpecification(RecipeCriteria criteria) {
        Specification<RecipeEntity> specification = Specification.where(null);

        if (criteria.getId() != null) {
            specification = specification.and(queryBuilder.buildFilterSpecification(criteria.getId(), RecipeEntity_.id));
        }
        if (criteria.getCategory() != null) {
            specification = specification.and(queryBuilder.buildFilterSpecification(criteria.getCategory(), RecipeEntity_.category));
        }
        if (criteria.getServings() != null) {
            specification = specification.and(queryBuilder.buildFilterSpecification(criteria.getServings(), RecipeEntity_.servings));
        }
        if (criteria.getInstructions() != null) {
            specification = specification.and(queryBuilder.buildFilterSpecification(criteria.getInstructions(), RecipeEntity_.instructions));
        }
        if (criteria.getIngredients() != null) {
            specification = specification.and(
                    queryBuilder.buildSpecification(
                            criteria.getIngredients(),
                            root -> root.join(RecipeEntity_.ingredients, JoinType.LEFT).get(IngredientEntity_.name)
                    )
            );
        }

        return specification;
    }

    @Override
    public boolean existsById(Long id){
        return jpaRecipeRepository.existsById(id);
    }

}