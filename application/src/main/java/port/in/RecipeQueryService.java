package port.in;


import domain.model.Recipe;
import impl.RecipeCriteria;
import impl.RecipeSearchCriteria;

import java.util.List;


public interface RecipeQueryService {
    /**
     * Find recipes based on structured search criteria.
     */
   List<Recipe> findByCriteria(RecipeCriteria searchCriteria, int page, int size);
}