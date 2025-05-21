package port.out;


import domain.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ingredient entity.
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
