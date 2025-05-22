package repository;


import entity.IngredientEntity;
import domain.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import port.out.IngredientRepository;

import java.util.Optional;

import entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaIngredientRepository extends JpaRepository<IngredientEntity, Long> {
}