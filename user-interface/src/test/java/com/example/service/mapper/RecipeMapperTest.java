package com.example.service.mapper;

import com.example.userinterface.mapper.RecipeMapper;
import com.example.userinterface.mapper.RecipeMapperImpl;
import org.junit.jupiter.api.BeforeEach;

class RecipeMapperTest {

    private RecipeMapper recipeMapper;

    @BeforeEach
    public void setUp() {
        recipeMapper = new RecipeMapperImpl();
    }
}
