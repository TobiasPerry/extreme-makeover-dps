package com.example.boot;

import com.example.userinterface.mapper.IngredientMapper;
import com.example.userinterface.mapper.RecipeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.application.port.in.IngredientService;
import com.example.application.port.in.RecipeQueryService;
import com.example.application.port.in.RecipeService;
import com.example.application.port.out.IngredientRepository;
import com.example.application.port.out.RecipeRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
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
