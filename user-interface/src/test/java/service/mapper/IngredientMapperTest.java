package service.mapper;

import mapper.IngredientMapper;
import mapper.IngredientMapperImpl;
import org.junit.jupiter.api.BeforeEach;

class IngredientMapperTest {

    private IngredientMapper ingredientMapper;

    @BeforeEach
    public void setUp() {
        ingredientMapper = new IngredientMapperImpl();
    }
}
