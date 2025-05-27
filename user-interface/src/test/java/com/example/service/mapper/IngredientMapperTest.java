package com.example.service.mapper;

import com.example.userinterface.mapper.IngredientMapper;
import com.example.userinterface.mapper.IngredientMapperImpl;
import org.junit.jupiter.api.BeforeEach;

class IngredientMapperTest {

    private IngredientMapper ingredientMapper;

    @BeforeEach
    public void setUp() {
        ingredientMapper = new IngredientMapperImpl();
    }
}
