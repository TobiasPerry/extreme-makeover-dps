package com.example.infrastructure.repository;

import com.example.domain.model.Ingredient;
import com.example.infrastructure.entity.IngredientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientRepositoryAdapterTest {

    @Mock
    private JpaIngredientRepository repository;

    @InjectMocks
    private IngredientRepositoryAdapter adapter;

    private IngredientEntity ingredientEntity;
    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        ingredientEntity = new IngredientEntity();
        ingredientEntity.setId(1L);
        ingredientEntity.setName("Test Ingredient");
        ingredientEntity.setRecipes(new HashSet<>());

        ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Test Ingredient");
        ingredient.setRecipes(new HashSet<>());
    }

    @Test
    void findById_ExistingId_ReturnsIngredient() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(ingredientEntity));

        // Act
        Optional<Ingredient> result = adapter.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(ingredient.getId(), result.get().getId());
        assertEquals(ingredient.getName(), result.get().getName());
    }

    @Test
    void findById_NonExistentId_ReturnsEmpty() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Ingredient> result = adapter.findById(1L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_ReturnsListOfIngredients() {
        // Arrange
        when(repository.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<>(Arrays.asList(ingredientEntity)));

        // Act
        List<Ingredient> result = adapter.findAll(0, 10);

        // Assert
        assertEquals(1, result.size());
        assertEquals(ingredient.getId(), result.get(0).getId());
        assertEquals(ingredient.getName(), result.get(0).getName());
    }

    @Test
    void save_NewIngredient_ReturnsSavedIngredient() {
        // Arrange
        when(repository.save(any(IngredientEntity.class))).thenReturn(ingredientEntity);

        // Act
        Ingredient result = adapter.save(ingredient);

        // Assert
        assertNotNull(result);
        assertEquals(ingredient.getId(), result.getId());
        assertEquals(ingredient.getName(), result.getName());
    }

    @Test
    void deleteById_ExistingIngredient_DeletesSuccessfully() {
        // Arrange
        doNothing().when(repository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> adapter.deleteById(1L));
        verify(repository).deleteById(1L);
    }

    @Test
    void existsById_WhenExists_ReturnsTrue() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = adapter.existsById(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_WhenNotExists_ReturnsFalse() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(false);

        // Act
        boolean result = adapter.existsById(1L);

        // Assert
        assertFalse(result);
    }
} 