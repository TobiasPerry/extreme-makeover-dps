package com.example.application.impl;

import com.example.application.port.out.IngredientRepository;
import com.example.domain.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    @Mock
    private IngredientRepository ingredientRepository;

    private IngredientServiceImpl ingredientService;

    @BeforeEach
    void setUp() {
        ingredientService = new IngredientServiceImpl(ingredientRepository);
    }

    @Test
    void save_ShouldSaveAndReturnIngredient() {
        // Arrange
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Test Ingredient");

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

        // Act
        Ingredient savedIngredient = ingredientService.save(ingredient);

        // Assert
        assertNotNull(savedIngredient);
        assertEquals("Test Ingredient", savedIngredient.getName());
        verify(ingredientRepository).save(ingredient);
    }

    @Test
    void update_ShouldUpdateAndReturnIngredient() {
        // Arrange
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Updated Ingredient");

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

        // Act
        Ingredient updatedIngredient = ingredientService.update(ingredient);

        // Assert
        assertNotNull(updatedIngredient);
        assertEquals(1L, updatedIngredient.getId());
        assertEquals("Updated Ingredient", updatedIngredient.getName());
        verify(ingredientRepository).save(ingredient);
    }

    @Test
    void findAll_ShouldReturnListOfIngredients() {
        // Arrange
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setName("Ingredient 1");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setName("Ingredient 2");

        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);
        when(ingredientRepository.findAll(0, 10)).thenReturn(ingredients);

        // Act
        List<Ingredient> foundIngredients = ingredientService.findAll(0, 10);

        // Assert
        assertNotNull(foundIngredients);
        assertEquals(2, foundIngredients.size());
        assertEquals("Ingredient 1", foundIngredients.get(0).getName());
        assertEquals("Ingredient 2", foundIngredients.get(1).getName());
        verify(ingredientRepository).findAll(0, 10);
    }

    @Test
    void findOne_WhenIngredientExists_ShouldReturnIngredient() {
        // Arrange
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Test Ingredient");

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

        // Act
        Optional<Ingredient> foundIngredient = ingredientService.findOne(1L);

        // Assert
        assertTrue(foundIngredient.isPresent());
        assertEquals(1L, foundIngredient.get().getId());
        assertEquals("Test Ingredient", foundIngredient.get().getName());
        verify(ingredientRepository).findById(1L);
    }

    @Test
    void findOne_WhenIngredientDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(ingredientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Ingredient> foundIngredient = ingredientService.findOne(1L);

        // Assert
        assertTrue(foundIngredient.isEmpty());
        verify(ingredientRepository).findById(1L);
    }

    @Test
    void delete_ShouldDeleteIngredient() {
        // Arrange
        Long ingredientId = 1L;

        // Act
        ingredientService.delete(ingredientId);

        // Assert
        verify(ingredientRepository).deleteById(ingredientId);
    }
} 