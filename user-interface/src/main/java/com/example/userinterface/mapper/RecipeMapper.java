package com.example.userinterface.mapper;


import com.example.domain.model.Recipe;
import com.example.userinterface.dto.RecipeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Recipe} and its DTO {@link RecipeDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecipeMapper extends EntityMapper<RecipeDTO, Recipe> {
    @Mapping(target = "ingredients", source = "ingredients")
    RecipeDTO toDto(Recipe s);

}
