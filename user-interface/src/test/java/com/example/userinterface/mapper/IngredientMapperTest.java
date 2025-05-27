package com.example.userinterface.mapper;

import com.example.domain.model.Ingredient;
import com.example.userinterface.dto.IngredientDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IngredientMapperTest {

    private IngredientMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new IngredientMapperImpl();
    }

    @Test
    void toDto_ShouldMapCorrectly() {
        // Arrange
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Test Ingredient");

        // Act
        IngredientDTO dto = mapper.toDto(ingredient);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals("Test Ingredient", dto.name());
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        // Arrange
        IngredientDTO dto = new IngredientDTO(1L, "Test Ingredient");

        // Act
        Ingredient ingredient = mapper.toEntity(dto);

        // Assert
        assertNotNull(ingredient);
        assertEquals(1L, ingredient.getId());
        assertEquals("Test Ingredient", ingredient.getName());
    }


} 