package com.example;

import mapper.IngredientMapper;
import mapper.RecipeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import port.in.IngredientService;
import port.in.RecipeQueryService;
import port.in.RecipeService;
import port.out.IngredientRepository;
import port.out.RecipeRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private RecipeQueryService recipeQBService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private IngredientMapper ingredientMapper;


    @Test
    void contextLoads() {
        assertNotNull(recipeQBService);
        assertNotNull(recipeService);
        assertNotNull(ingredientService);
        assertNotNull(recipeRepository);
        assertNotNull(ingredientRepository);
        assertNotNull(recipeMapper);
        assertNotNull(ingredientMapper);
    }

}
