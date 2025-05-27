package com.example.userinterface.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import com.example.application.port.out.IngredientRepository;
import com.example.application.port.out.RecipeRepository;
import com.example.application.port.in.IngredientService;
import com.example.application.port.in.RecipeService;
import com.example.application.port.in.RecipeQueryService;
import com.example.application.impl.IngredientServiceImpl;
import com.example.application.impl.RecipeServiceImpl;
import com.example.application.impl.RecipeQueryServiceImpl;

@TestConfiguration
@ComponentScan(basePackages = {
    "com.example.userinterface.mapper",
    "com.example.userinterface.rest",
    "com.example.userinterface.dto",
    "com.example.application",
    "com.example.infrastructure.repository"
})
public class TestConfig {
    
    @MockBean
    private IngredientRepository ingredientRepository;
    
    @MockBean
    private RecipeRepository recipeRepository;
    
    @MockBean
    private IngredientService ingredientService;
    
    @MockBean
    private RecipeService recipeService;
    
    @MockBean
    private RecipeQueryService recipeQueryService;
} 