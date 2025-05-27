package com.example.userinterface.rest;

import com.example.domain.model.Ingredient;
import com.example.userinterface.dto.IngredientDTO;
import com.example.userinterface.mapper.IngredientMapper;
import com.example.application.port.in.IngredientService;
import com.example.application.port.out.IngredientRepository;
import com.example.userinterface.rest.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IngredientResourceTest {

    @Mock
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @InjectMocks
    private IngredientResource ingredientResource;

    private IngredientDTO ingredientDTO;
    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test data
        ingredientDTO = new IngredientDTO(1L, "Test Ingredient");
        ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Test Ingredient");
    }

    @Test
    void createIngredient_Success() throws URISyntaxException {
        // Arrange
        IngredientDTO newIngredientDTO = new IngredientDTO(null, "New Ingredient");
        Ingredient newIngredient = new Ingredient();
        newIngredient.setName("New Ingredient");

        when(ingredientMapper.toEntity(newIngredientDTO)).thenReturn(newIngredient);
        when(ingredientService.save(any(Ingredient.class))).thenReturn(newIngredient);
        when(ingredientMapper.toDto(newIngredient)).thenReturn(newIngredientDTO);

        // Act
        ResponseEntity<IngredientDTO> response = ingredientResource.createIngredient(newIngredientDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Ingredient", response.getBody().name());
        verify(ingredientService).save(any(Ingredient.class));
    }

    @Test
    void createIngredient_WithExistingId_ThrowsException() {
        // Arrange
        IngredientDTO existingIngredientDTO = new IngredientDTO(1L, "Existing Ingredient");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> ingredientResource.createIngredient(existingIngredientDTO));
        verify(ingredientService, never()).save(any(Ingredient.class));
    }

    @Test
    void updateIngredient_Success() throws URISyntaxException {
        // Arrange
        when(ingredientRepository.existsById(1L)).thenReturn(true);
        when(ingredientMapper.toEntity(ingredientDTO)).thenReturn(ingredient);
        when(ingredientService.update(any(Ingredient.class))).thenReturn(ingredient);
        when(ingredientMapper.toDto(ingredient)).thenReturn(ingredientDTO);

        // Act
        ResponseEntity<IngredientDTO> response = ingredientResource.updateIngredient(1L, ingredientDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        verify(ingredientService).update(any(Ingredient.class));
    }

    @Test
    void updateIngredient_NonExistentId_ThrowsException() {
        // Arrange
        when(ingredientRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> ingredientResource.updateIngredient(1L, ingredientDTO));
        verify(ingredientService, never()).update(any(Ingredient.class));
    }

    @Test
    void getAllIngredients_Success() {
        // Arrange
        List<Ingredient> ingredients = Arrays.asList(ingredient);
        when(ingredientService.findAll(10, 0)).thenReturn(ingredients);
        when(ingredientMapper.toDto(any(Ingredient.class))).thenReturn(ingredientDTO);

        // Act
        ResponseEntity<List<IngredientDTO>> response = ingredientResource.getAllIngredients(10, 0);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(ingredientService).findAll(10, 0);
    }

    @Test
    void getIngredient_Success() {
        // Arrange
        when(ingredientService.findOne(1L)).thenReturn(Optional.of(ingredient));
        when(ingredientMapper.toDto(ingredient)).thenReturn(ingredientDTO);

        // Act
        ResponseEntity<IngredientDTO> response = ingredientResource.getIngredient(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        verify(ingredientService).findOne(1L);
    }

    @Test
    void getIngredient_NotFound() {
        // Arrange
        when(ingredientService.findOne(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<IngredientDTO> response = ingredientResource.getIngredient(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(ingredientService).findOne(1L);
    }

    @Test
    void deleteIngredient_Success() {
        // Act
        ResponseEntity<Void> response = ingredientResource.deleteIngredient(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ingredientService).delete(1L);
    }
} 