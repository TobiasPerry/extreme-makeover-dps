package port.out;

import domain.model.Recipe;
import impl.RecipeCriteria;
import impl.RecipeSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.model.Recipe;
import java.util.List;
import java.util.Optional;

public interface RecipeRepository {
    Optional<Recipe> findById(Long id);
    List<Recipe> findAll(int page, int size);
    List<Recipe> findAllWithIngredientsByIds(List<Long> ids);
    Recipe save(Recipe recipe);
    void deleteById(Long id);

    List<Recipe> findByCriteria(RecipeCriteria criteria, int page, int size);
}

