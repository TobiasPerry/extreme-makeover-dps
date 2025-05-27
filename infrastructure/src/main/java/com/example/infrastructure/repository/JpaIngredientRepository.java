package com.example.infrastructure.repository;


import com.example.infrastructure.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaIngredientRepository extends JpaRepository<IngredientEntity, Long> {
}