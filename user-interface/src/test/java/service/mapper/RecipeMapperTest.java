package service.mapper;

import mapper.RecipeMapper;
import mapper.RecipeMapperImpl;
import org.junit.jupiter.api.BeforeEach;

class RecipeMapperTest {

    private RecipeMapper recipeMapper;

    @BeforeEach
    public void setUp() {
        recipeMapper = new RecipeMapperImpl();
    }
}
