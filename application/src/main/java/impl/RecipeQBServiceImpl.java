package impl;


import domain.model.Ingredient;
import domain.model.Recipe;
import dto.IngredientDTO;
import dto.RecipeDTO;
import jakarta.persistence.criteria.JoinType;
import mapper.RecipeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.in.IngredientService;
import port.in.RecipeQBService;
import port.out.IngredientRepository;
import port.out.RecipeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RecipeQBServiceImpl extends QueryBuilderService<Recipe> implements RecipeQBService {

    private final Logger log = LoggerFactory.getLogger(RecipeQBService.class);

    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;

    public RecipeQBServiceImpl(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeDTO> findByCriteria(RecipeCriteria criteria, Pageable pageable) {
        log.debug("Request to find Recipes by criteria: " + criteria);
        Specification<Recipe> specification = buildSpecification(criteria);
        Page<Recipe> result = getPaginatedRecipes(specification, pageable);
        return result.map(recipeMapper::toDto);
    }

    /**
     * Function to convert {@link RecipeCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    private Specification<Recipe> buildSpecification(RecipeCriteria criteria) {
        Specification<Recipe> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildFilterSpecification(criteria.getId(), Recipe_.id));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildFilterSpecification(criteria.getCategory(), Recipe_.category));
            }
            if (criteria.getServings() != null) {
                specification = specification.and(buildFilterSpecification(criteria.getServings(), Recipe_.servings));
            }
            if (criteria.getInstructions() != null) {
                specification = specification.and(buildFilterSpecification(criteria.getInstructions(), Recipe_.instructions));
            }
            if (criteria.getIngredients() != null) {
                specification = specification.and(
                        buildSpecification(
                                criteria.getIngredients(),
                                root -> root.join(Recipe_.ingredients, JoinType.LEFT).get(Ingredient_.name)
                        )
                );
            }
        }
        return specification;
    }

    private Page<Recipe> getPaginatedRecipes(Specification<Recipe> specification, Pageable pageable) {
        Page<Recipe> recipePage = recipeRepository.findAll(specification, pageable);
        List<Long> recipeIds = recipePage.getContent().stream().map(Recipe::getId).collect(Collectors.toList());
        List<Recipe> recipesWithIngredients = recipeRepository.findAllWithIngredientsByIds(recipeIds);

        return new PageImpl<>(recipesWithIngredients, pageable, recipePage.getTotalElements());
    }

    /**
     * Service Implementation for managing {@link Ingredient}.
     */
    @Service
    @Transactional
    public static class IngredientServiceImpl implements IngredientService {

        private final Logger log = LoggerFactory.getLogger(IngredientServiceImpl.class);

        private final IngredientRepository ingredientRepository;

        private final mapper.IngredientMapper ingredientMapper;

        public IngredientServiceImpl(IngredientRepository ingredientRepository, mapper.IngredientMapper ingredientMapper) {
            this.ingredientRepository = ingredientRepository;
            this.ingredientMapper = ingredientMapper;
        }

        @Override
        public IngredientDTO save(IngredientDTO ingredientDTO) {
            log.debug("Request to save Ingredient : {}", ingredientDTO);
            Ingredient ingredient = ingredientMapper.toEntity(ingredientDTO);
            ingredient = ingredientRepository.save(ingredient);
            return ingredientMapper.toDto(ingredient);
        }

        @Override
        public IngredientDTO update(IngredientDTO ingredientDTO) {
            log.debug("Request to update Ingredient : {}", ingredientDTO);
            Ingredient ingredient = ingredientMapper.toEntity(ingredientDTO);
            ingredient = ingredientRepository.save(ingredient);
            return ingredientMapper.toDto(ingredient);
        }

        @Override
        @Transactional(readOnly = true)
        public Page<IngredientDTO> findAll(Pageable pageable) {
            log.debug("Request to get all Ingredients");
            return ingredientRepository.findAll(pageable).map(ingredientMapper::toDto);
        }

        @Override
        @Transactional(readOnly = true)
        public Optional<IngredientDTO> findOne(Long id) {
            log.debug("Request to get Ingredient : {}", id);
            return ingredientRepository.findById(id).map(ingredientMapper::toDto);
        }

        @Override
        public void delete(Long id) {
            log.debug("Request to delete Ingredient : {}", id);
            ingredientRepository.deleteById(id);
        }
    }
}
