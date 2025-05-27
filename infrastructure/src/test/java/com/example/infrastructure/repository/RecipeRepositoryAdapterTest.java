package com.example.infrastructure.repository;

import com.example.application.impl.RecipeCriteria;
import com.example.domain.model.Recipe;
import com.example.domain.model.RecipeCategory;
import com.example.infrastructure.entity.RecipeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeRepositoryAdapterTest {

    @Mock
    private JpaRecipeRepository jpaRecipeRepository;

    @Mock
    private QueryBuilderService<RecipeEntity> queryBuilder;

    private RecipeRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new RecipeRepositoryAdapter(jpaRecipeRepository, queryBuilder);
    }

    @Test
    void findById_WhenExists_ReturnsRecipe() {
        // Arrange
        Long id = 1L;
        RecipeEntity entity = new RecipeEntity();
        entity.setId(id);
        entity.setCategory(RecipeCategory.vegetarian);
        entity.setServings(4);
        entity.setInstructions("Test instructions");
        when(jpaRecipeRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        Optional<Recipe> result = adapter.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals(RecipeCategory.vegetarian, result.get().getCategory());
        assertEquals(4, result.get().getServings());
        assertEquals("Test instructions", result.get().getInstructions());
        verify(jpaRecipeRepository).findById(id);
    }

    @Test
    void findById_WhenNotExists_ReturnsEmpty() {
        // Arrange
        Long id = 1L;
        when(jpaRecipeRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Recipe> result = adapter.findById(id);

        // Assert
        assertTrue(result.isEmpty());
        verify(jpaRecipeRepository).findById(id);
    }

    @Test
    void findAll_ReturnsListOfRecipes() {
        // Arrange
        RecipeEntity entity1 = new RecipeEntity();
        entity1.setId(1L);
        entity1.setCategory(RecipeCategory.vegetarian);
        entity1.setServings(2);

        RecipeEntity entity2 = new RecipeEntity();
        entity2.setId(2L);
        entity2.setCategory(RecipeCategory.nonvegetarian);
        entity2.setServings(4);

        when(jpaRecipeRepository.findAll(any(PageRequest.class)))
            .thenReturn(new PageImpl<>(Arrays.asList(entity1, entity2)));

        // Act
        List<Recipe> result = adapter.findAll(0, 10);

        // Assert
        assertEquals(2, result.size());
        assertEquals(RecipeCategory.vegetarian, result.get(0).getCategory());
        assertEquals(RecipeCategory.nonvegetarian, result.get(1).getCategory());
        verify(jpaRecipeRepository).findAll(any(PageRequest.class));
    }

    @Test
    void findAllWithIngredientsByIds_ReturnsRecipesWithIngredients() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L);
        RecipeEntity entity1 = new RecipeEntity();
        entity1.setId(1L);
        entity1.setCategory(RecipeCategory.vegetarian);

        RecipeEntity entity2 = new RecipeEntity();
        entity2.setId(2L);
        entity2.setCategory(RecipeCategory.nonvegetarian);

        when(jpaRecipeRepository.findAllWithIngredientsByIds(ids))
            .thenReturn(Arrays.asList(entity1, entity2));

        // Act
        List<Recipe> result = adapter.findAllWithIngredientsByIds(ids);

        // Assert
        assertEquals(2, result.size());
        assertEquals(RecipeCategory.vegetarian, result.get(0).getCategory());
        assertEquals(RecipeCategory.nonvegetarian, result.get(1).getCategory());
        verify(jpaRecipeRepository).findAllWithIngredientsByIds(ids);
    }

    @Test
    void save_ReturnsSavedRecipe() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setCategory(RecipeCategory.vegetarian);
        recipe.setServings(4);
        recipe.setInstructions("Test instructions");
        
        RecipeEntity entity = new RecipeEntity();
        entity.setId(1L);
        entity.setCategory(RecipeCategory.vegetarian);
        entity.setServings(4);
        entity.setInstructions("Test instructions");

        when(jpaRecipeRepository.save(any(RecipeEntity.class))).thenReturn(entity);

        // Act
        Recipe result = adapter.save(recipe);

        // Assert
        assertNotNull(result);
        assertEquals(recipe.getId(), result.getId());
        assertEquals(recipe.getCategory(), result.getCategory());
        assertEquals(recipe.getServings(), result.getServings());
        assertEquals(recipe.getInstructions(), result.getInstructions());
        verify(jpaRecipeRepository).save(any(RecipeEntity.class));
    }

    @Test
    void deleteById_CallsRepository() {
        // Arrange
        Long id = 1L;

        // Act
        adapter.deleteById(id);

        // Assert
        verify(jpaRecipeRepository).deleteById(id);
    }

    @Test
    void findByCriteria_ReturnsFilteredRecipes() {
        // Arrange
        RecipeCriteria criteria = new RecipeCriteria();
        RecipeCriteria.RecipeCategoryFilter categoryFilter = new RecipeCriteria.RecipeCategoryFilter();
        categoryFilter.setEq(RecipeCategory.vegetarian);
        criteria.setCategory(categoryFilter);
        
        RecipeEntity entity = new RecipeEntity();
        entity.setId(1L);
        entity.setCategory(RecipeCategory.vegetarian);
        entity.setServings(4);

        when(queryBuilder.buildFilterSpecification(any(), any())).thenReturn(Specification.where(null));
        when(jpaRecipeRepository.findAll(any(Specification.class), any(PageRequest.class)))
            .thenReturn(new PageImpl<>(List.of(entity)));

        // Act
        List<Recipe> result = adapter.findByCriteria(criteria, 0, 10);

        // Assert
        assertEquals(1, result.size());
        assertEquals(RecipeCategory.vegetarian, result.get(0).getCategory());
        assertEquals(4, result.get(0).getServings());
        verify(jpaRecipeRepository).findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    void existsById_WhenExists_ReturnsTrue() {
        // Arrange
        Long id = 1L;
        when(jpaRecipeRepository.existsById(id)).thenReturn(true);

        // Act
        boolean result = adapter.existsById(id);

        // Assert
        assertTrue(result);
        verify(jpaRecipeRepository).existsById(id);
    }

    @Test
    void existsById_WhenNotExists_ReturnsFalse() {
        // Arrange
        Long id = 1L;
        when(jpaRecipeRepository.existsById(id)).thenReturn(false);

        // Act
        boolean result = adapter.existsById(id);

        // Assert
        assertFalse(result);
        verify(jpaRecipeRepository).existsById(id);
    }
} 