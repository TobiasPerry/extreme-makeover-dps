package service.impl;


import domain.model.Recipe;
import dto.RecipeDTO;
import impl.RecipeServiceImpl;
import mapper.RecipeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import port.out.RecipeRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        RecipeDTO recipeDTO = new RecipeDTO(null, null, null, null, null);
        Recipe recipe = new Recipe();
        when(recipeMapper.toEntity(recipeDTO)).thenReturn(recipe);
        when(recipeRepository.save(recipe)).thenReturn(recipe);
        when(recipeMapper.toDto(recipe)).thenReturn(recipeDTO);

        Recipe result = recipeService.save(recipe);

        assertEquals(recipe, result);
        verify(recipeRepository, times(1)).save(recipe);
    }

    @Test
    public void testUpdate() {
        RecipeDTO recipeDTO = new RecipeDTO(null, null, null, null, null);
        Recipe recipe = new Recipe();
        when(recipeMapper.toEntity(recipeDTO)).thenReturn(recipe);
        when(recipeRepository.save(recipe)).thenReturn(recipe);
        when(recipeMapper.toDto(recipe)).thenReturn(recipeDTO);

        Recipe result = recipeService.update(recipe);

        assertEquals(recipe, result);
        verify(recipeRepository, times(1)).save(recipe);
    }

    @Test
    public void testFindOne() {
        Long id = 1L;
        Recipe recipe = new Recipe();
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));

        Optional<Recipe> result = recipeService.findOne(id);
        assertEquals(recipe, result.orElse(null));
        verify(recipeRepository, times(1)).findById(id);
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        recipeService.delete(id);
        verify(recipeRepository, times(1)).deleteById(id);
    }
}
