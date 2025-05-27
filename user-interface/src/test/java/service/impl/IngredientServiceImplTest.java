package service.impl;


import domain.model.Ingredient;
import dto.IngredientDTO;
import impl.IngredientServiceImpl;
import mapper.IngredientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import port.out.IngredientRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @InjectMocks
    private IngredientServiceImpl ingredientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        IngredientDTO ingredientDTO = new IngredientDTO(1L, null);
        Ingredient ingredient = new Ingredient();
        when(ingredientMapper.toEntity(ingredientDTO)).thenReturn(ingredient);
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);
        when(ingredientMapper.toDto(ingredient)).thenReturn(ingredientDTO);

        Ingredient result = ingredientService.save(ingredientMapper.toEntity(ingredientDTO));

        assertEquals(ingredient, result);
    }

    @Test
    public void testUpdate() {
        IngredientDTO ingredientDTO = new IngredientDTO(1L, null);
        Ingredient ingredient = new Ingredient();
        when(ingredientMapper.toEntity(ingredientDTO)).thenReturn(ingredient);
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);
        when(ingredientMapper.toDto(ingredient)).thenReturn(ingredientDTO);

        Ingredient result = ingredientService.update(ingredientMapper.toEntity(ingredientDTO));

        assertEquals(ingredient, result);
    }

    @Test
    public void testFindAll() {
        Pageable pageable = Pageable.unpaged();
        Ingredient ingredient = new Ingredient();
        IngredientDTO ingredientDTO = new IngredientDTO(1L, null);
        List<Ingredient> ingredientPage = new ArrayList<>(Collections.singletonList(ingredient));
        when(ingredientRepository.findAll(0,1)).thenReturn(ingredientPage);
        when(ingredientMapper.toDto(ingredient)).thenReturn(ingredientDTO);

        List<Ingredient> result = ingredientService.findAll(0,1);

        assertEquals(1, result.size());
        assertEquals(ingredient, result.get(0));
    }

    @Test
    public void testFindOne() {
        Long id = 1L;
        Ingredient ingredient = new Ingredient();
        IngredientDTO ingredientDTO = new IngredientDTO(1L, null);
        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));
        when(ingredientMapper.toDto(ingredient)).thenReturn(ingredientDTO);

        Optional<Ingredient> result = ingredientService.findOne(id);

        assertEquals(ingredient, result.orElse(null));
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        ingredientService.delete(id);
        verify(ingredientRepository, times(1)).deleteById(id);
    }
}
