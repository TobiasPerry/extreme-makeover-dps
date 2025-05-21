package port.in;


import dto.RecipeDTO;
import impl.RecipeCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link domain.model.Recipe}
 */
public interface RecipeQBService {

    /**
     * Find recipes by criteria, all if criteria is null
     *
     * @param criteria the criteria to query
     * @param pageable pageable query params
     * @return the entity paginated.
     */
    @Transactional(readOnly = true)
    Page<RecipeDTO> findByCriteria(RecipeCriteria criteria, Pageable pageable);
}
