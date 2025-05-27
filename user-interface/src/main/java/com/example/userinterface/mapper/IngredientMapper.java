package com.example.userinterface.mapper;

import com.example.domain.model.Ingredient;
import com.example.userinterface.dto.IngredientDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Ingredient} and its DTO {@link IngredientDTO}.
 */
@Mapper(componentModel = "spring")
public interface IngredientMapper extends EntityMapper<IngredientDTO, Ingredient> {
    @Override
    IngredientDTO toDto(Ingredient ingredient);

    @Override
    Ingredient toEntity(IngredientDTO ingredientDTO);
}
