package com.example.infrastructure.repository;

import com.example.application.impl.Filter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class QueryBuilderService<E> {
    public <X> Specification<E> buildFilterSpecification(Filter<X> filter, SingularAttribute<? super E, X> field) {
        if (String.class.isAssignableFrom(field.getJavaType())) {
            return buildSpecification((Filter<String>) filter, root -> root.get((SingularAttribute<? super E, String>) field));
        } else {
            return buildSpecificationGeneric(filter, root -> root.get(field));
        }
    }

    public <X> Specification<E> buildSpecificationGeneric(Filter<X> filter, Function<Root<E>, Expression<X>> metaclassFn) {
        if (filter.getEq() != null) {
            return (root, query, builder) -> builder.equal(metaclassFn.apply(root), filter.getEq());
        } else if (filter.getNeq() != null) {
            return (root, query, builder) -> builder.not(builder.equal(metaclassFn.apply(root), filter.getNeq()));
        } else if (filter.getIn() != null) {
            return (root, query, builder) -> {
                CriteriaBuilder.In<X> in = builder.in(metaclassFn.apply(root));
                filter.getIn().forEach(in::value);
                return in;
            };
        } else if (filter.getNin() != null) {
            return (root, query, builder) -> {
                CriteriaBuilder.In<X> in = builder.in(metaclassFn.apply(root));
                filter.getNin().forEach(in::value);
                return builder.not(in);
            };
        }
        return null;
    }

    public Specification<E> buildSpecification(Filter<String> filter, Function<Root<E>, Expression<String>> metaclassFn) {
        if (filter.getContains() != null) {
            return (root, query, builder) -> builder.like(builder.upper(metaclassFn.apply(root)), wrapLikeQuery(filter.getContains()));
        } else if (filter.getNotContains() != null) {
            return (root, query, builder) -> builder.not(builder.like(builder.upper(metaclassFn.apply(root)), wrapLikeQuery(filter.getNotContains())));
        } else {
            return buildSpecificationGeneric(filter, metaclassFn);
        }
    }

    public String wrapLikeQuery(String txt) {
        return "%" + txt.toUpperCase() + '%';
    }
}
