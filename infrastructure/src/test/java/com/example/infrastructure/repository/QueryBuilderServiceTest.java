package com.example.infrastructure.repository;

import com.example.application.impl.Filter;
import com.example.infrastructure.entity.RecipeEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueryBuilderServiceTest {

    private QueryBuilderService<RecipeEntity> queryBuilder;

    @Mock
    private Root<RecipeEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private SingularAttribute<RecipeEntity, Long> idAttribute;

    @Mock
    private SingularAttribute<RecipeEntity, String> instructionsAttribute;

    @Mock
    private CriteriaBuilder.In<Object> inPredicate;

    @BeforeEach
    void setUp() {
        queryBuilder = new QueryBuilderService<>();
        lenient().when(idAttribute.getJavaType()).thenReturn(Long.class);
        lenient().when(instructionsAttribute.getJavaType()).thenReturn(String.class);
        lenient().when(builder.in(any())).thenReturn(inPredicate);
        lenient().when(inPredicate.value(any(Object.class))).thenReturn(inPredicate);
    }

    @Test
    void buildFilterSpecification_WithStringField_ReturnsStringSpecification() {
        // Arrange
        Filter<String> filter = new Filter<>();
        filter.setEq("test");

        // Act
        Specification<RecipeEntity> spec = queryBuilder.buildFilterSpecification(filter, instructionsAttribute);

        // Assert
        assertNotNull(spec);
        spec.toPredicate(root, query, builder);
        verify(builder).equal(any(), eq("test"));
    }

    @Test
    void buildFilterSpecification_WithNonStringField_ReturnsGenericSpecification() {
        // Arrange
        Filter<Long> filter = new Filter<>();
        filter.setEq(1L);

        // Act
        Specification<RecipeEntity> spec = queryBuilder.buildFilterSpecification(filter, idAttribute);

        // Assert
        assertNotNull(spec);
        spec.toPredicate(root, query, builder);
        verify(builder).equal(any(), eq(1L));
    }

    @Test
    void buildSpecificationGeneric_WithEquals_ReturnsEqualSpecification() {
        // Arrange
        Filter<String> filter = new Filter<>();
        filter.setEq("test");

        // Act
        Specification<RecipeEntity> spec = queryBuilder.buildSpecificationGeneric(filter, root -> root.get(instructionsAttribute));

        // Assert
        assertNotNull(spec);
        spec.toPredicate(root, query, builder);
        verify(builder).equal(any(), eq("test"));
    }

    @Test
    void buildSpecificationGeneric_WithNotEquals_ReturnsNotEqualSpecification() {
        // Arrange
        Filter<String> filter = new Filter<>();
        filter.setNeq("test");

        // Act
        Specification<RecipeEntity> spec = queryBuilder.buildSpecificationGeneric(filter, root -> root.get(instructionsAttribute));

        // Assert
        assertNotNull(spec);
        spec.toPredicate(root, query, builder);
        verify(builder).not(any());
    }

    @Test
    void buildSpecificationGeneric_WithIn_ReturnsInSpecification() {
        // Arrange
        Filter<String> filter = new Filter<>();
        filter.setIn(Arrays.asList("test1", "test2"));

        // Act
        Specification<RecipeEntity> spec = queryBuilder.buildSpecificationGeneric(filter, root -> root.get(instructionsAttribute));

        // Assert
        assertNotNull(spec);
        spec.toPredicate(root, query, builder);
        verify(builder).in(any());
        verify(inPredicate, times(2)).value(any(Object.class));
    }

    @Test
    void buildSpecificationGeneric_WithNotIn_ReturnsNotInSpecification() {
        // Arrange
        Filter<String> filter = new Filter<>();
        filter.setNin(Arrays.asList("test1", "test2"));

        // Act
        Specification<RecipeEntity> spec = queryBuilder.buildSpecificationGeneric(filter, root -> root.get(instructionsAttribute));

        // Assert
        assertNotNull(spec);
        spec.toPredicate(root, query, builder);
        verify(builder).in(any());
        verify(builder).not(any());
        verify(inPredicate, times(2)).value(any(Object.class));
    }

    @Test
    void buildSpecification_WithContains_ReturnsLikeSpecification() {
        // Arrange
        Filter<String> filter = new Filter<>();
        filter.setContains("test");

        // Act
        Specification<RecipeEntity> spec = queryBuilder.buildSpecification(filter, root -> root.get(instructionsAttribute));

        // Assert
        assertNotNull(spec);
        spec.toPredicate(root, query, builder);
        verify(builder).like(any(), eq("%TEST%"));
    }

    @Test
    void buildSpecification_WithNotContains_ReturnsNotLikeSpecification() {
        // Arrange
        Filter<String> filter = new Filter<>();
        filter.setNotContains("test");

        // Act
        Specification<RecipeEntity> spec = queryBuilder.buildSpecification(filter, root -> root.get(instructionsAttribute));

        // Assert
        assertNotNull(spec);
        spec.toPredicate(root, query, builder);
        verify(builder).not(any());
    }

    @Test
    void wrapLikeQuery_ReturnsWrappedString() {
        // Arrange
        String input = "test";

        // Act
        String result = queryBuilder.wrapLikeQuery(input);

        // Assert
        assertEquals("%TEST%", result);
    }
} 