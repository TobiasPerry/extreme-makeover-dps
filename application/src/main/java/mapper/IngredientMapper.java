package mapper;


import domain.model.Ingredient;
import dto.IngredientDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Ingredient} and its DTO {@link IngredientDTO}.
 */
@Mapper(componentModel = "spring")
public interface IngredientMapper extends EntityMapper<IngredientDTO, Ingredient> {
}
