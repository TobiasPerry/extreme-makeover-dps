package mapper;


import domain.model.Ingredient;
import domain.model.Recipe;
import dto.IngredientDTO;
import dto.RecipeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Recipe} and its DTO {@link RecipeDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecipeMapper extends EntityMapper<RecipeDTO, Recipe> {
    @Mapping(target = "ingredients", source = "ingredients")
    RecipeDTO toDto(Recipe s);

}
