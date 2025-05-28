package com.example.userinterface.rest;


import com.example.domain.model.Recipe;
import com.example.userinterface.mapper.RecipeMapper;
import com.example.userinterface.rest.exceptions.BadRequestException;
import com.example.userinterface.dto.RecipeDTO;
import com.example.application.port.in.RecipeQueryService;
import com.example.application.port.in.RecipeService;
import com.example.application.port.out.RecipeRepository;
import com.example.application.impl.RecipeCriteria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing
 */
@RestController
@RequestMapping("/api")
public class RecipeResource {

    private final Logger log = LoggerFactory.getLogger(RecipeResource.class);

    private static final String ENTITY_NAME = "Recipe";

    private final RecipeService recipeService;

    private final RecipeQueryService recipeQueryService;

    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;

    public RecipeResource(RecipeService recipeService, RecipeQueryService recipeQueryService, RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        System.out.println("RecipeController loaded");
        this.recipeService = recipeService;
        this.recipeQueryService = recipeQueryService;
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    /**
     * {@code POST  /recipes} : Create a new recipe.
     *
     * @param recipeDTO the recipeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recipeDTO, or with status {@code 400 (Bad Request)} if the recipe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PostMapping("/recipes")
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO) throws URISyntaxException {
        log.debug("REST request to save Recipe : {}", recipeDTO);
        if (recipeDTO.id() != null) {
            throw new BadRequestException("A new recipe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecipeDTO result = recipeMapper.toDto(recipeService.save(recipeMapper.toEntity(recipeDTO)));
        return ResponseEntity
                .created(new URI("/api/recipes/" + result.id()))
                .body(result);
    }

    /**
     * {@code PUT  /recipes/:id} : Updates an existing recipe.
     *
     * @param id        the id of the recipeDTO to save.
     * @param recipeDTO the recipeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipeDTO,
     * or with status {@code 400 (Bad Request)} if the recipeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recipeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recipes/{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody RecipeDTO recipeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Recipe : {}, {}", id, recipeDTO);
        if (recipeDTO.id() == null) {
            throw new BadRequestException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipeDTO.id())) {
            throw new BadRequestException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeRepository.existsById(id)) {
            throw new BadRequestException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecipeDTO result = recipeMapper.toDto(recipeService.update(recipeMapper.toEntity(recipeDTO)));
        return ResponseEntity
                .ok()
                .body(result);
    }

    /**
     * {@code GET  /recipes} : get recipes based on criteria or all.
     *
     * @param criteria the defining the filtering criteria.
     * @param pageSize the page size
     * @param pageNumber the page number.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recipes in body.
     */
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDTO>> getRecipesByCriteria(@RequestBody RecipeCriteria criteria,
                                                                @RequestParam int pageSize, @RequestParam int pageNumber) {
        log.debug("REST request to get Recipes by criteria");
        List<Recipe> recipes = recipeQueryService.findByCriteria(criteria, pageNumber, pageSize);
        List < RecipeDTO > dtoList = recipes.stream().map(recipeMapper::toDto).toList();

        return ResponseEntity.ok().body(dtoList);
    }

    /**
     * {@code GET  /recipes/:id} : get the "id" recipe.
     *
     * @param id the id of the recipeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable Long id) {
        log.debug("REST request to get Recipe : {}", id);
        Optional<Recipe> ingredient = recipeService.findOne(id);
        return ingredient.map(value -> ResponseEntity.ok().body(recipeMapper.toDto(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /recipes/:id} : delete the "id" recipe.
     *
     * @param id the id of the recipeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        log.debug("REST request to delete Recipe : {}", id);
        recipeService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
