package rest;


import domain.model.Ingredient;
import dto.IngredientDTO;
import mapper.IngredientMapper;
import port.in.IngredientService;
import port.out.IngredientRepository;
import rest.exceptions.BadRequestException;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing .
 */
@RestController
@RequestMapping("/api")
public class IngredientResource {

    private final Logger log = LoggerFactory.getLogger(IngredientResource.class);

    private static final String ENTITY_NAME = "Ingredient";

    private final IngredientService ingredientService;

    private final IngredientRepository ingredientRepository;

    private final IngredientMapper ingredientMapper;

    public IngredientResource(IngredientService ingredientService, IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientService = ingredientService;
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    /**
     * {@code POST  /ingredients} : Create a new ingredient.
     *
     * @param ingredientDTO the ingredientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingredientDTO, or with status {@code 400 (Bad Request)} if the ingredient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ingredients")
    public ResponseEntity<IngredientDTO> createIngredient(@Valid @RequestBody IngredientDTO ingredientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDTO);
        if (ingredientDTO.id() != null) {
            throw new BadRequestException("A new ingredient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (ingredientDTO.name() == null) {
            throw new BadRequestException("Name must be present", ENTITY_NAME, "namenull");
        }
        IngredientDTO result = ingredientMapper.toDto(ingredientService.save( ingredientMapper.toEntity(ingredientDTO) ));
        return ResponseEntity
                .created(new URI("/api/ingredients/" + result.id()))
                .body(result);
    }

    /**
     * {@code PUT  /ingredients/:id} : Updates an existing ingredient.
     *
     * @param id            the id of the ingredientDTO to save.
     * @param ingredientDTO the ingredientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredientDTO,
     * or with status {@code 400 (Bad Request)} if the ingredientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingredientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ingredients/{id}")
    public ResponseEntity<IngredientDTO> updateIngredient(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody IngredientDTO ingredientDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ingredient : {}, {}", id, ingredientDTO);
        if (ingredientDTO.id() == null) {
            throw new BadRequestException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredientDTO.id())) {
            throw new BadRequestException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredientRepository.existsById(id)) {
            throw new BadRequestException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IngredientDTO result = ingredientMapper.toDto(ingredientService.update(ingredientMapper.toEntity(ingredientDTO) ));
        return ResponseEntity
                .ok()
                .body(result);
    }

    /**
     * {@code GET  /ingredients} : get all the ingredients.
     * @param pageNumber the page number.
     * @param pageSize the page size
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingredients in body.
     */
    @GetMapping("/ingredients")
    public ResponseEntity<List<IngredientDTO>> getAllIngredients(@RequestParam int pageSize, @RequestParam int pageNumber) {
        log.debug("REST request to get a list of Ingredients");

        List<Ingredient> ingredients = ingredientService.findAll(pageSize, pageNumber);

        List<IngredientDTO> dtoList = ingredients.stream()
                .map(ingredientMapper::toDto)
                .toList();

        return ResponseEntity.ok().body(dtoList);
    }


    /**
     * {@code GET  /ingredients/:id} : get the "id" ingredient.
     *
     * @param id the id of the ingredientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingredientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ingredients/{id}")
    public ResponseEntity<IngredientDTO> getIngredient(@PathVariable Long id) {
        log.debug("REST request to get Ingredient : {}", id);
        Optional<Ingredient> ingredient = ingredientService.findOne(id);
        return ingredient.map(value -> ResponseEntity.ok().body(ingredientMapper.toDto(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /ingredients/:id} : delete the "id" ingredient.
     *
     * @param id the id of the ingredientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ingredients/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        log.debug("REST request to delete Ingredient : {}", id);
        ingredientService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
