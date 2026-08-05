package com.kijoukoi.app.infrastructure;

import com.kijoukoi.app.domain.Blade;
import com.kijoukoi.app.domain.Rubber;
import com.kijoukoi.app.domain.dto.FilterDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DynamicEquipmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Blade> searchBlades(List<FilterDTO> filters) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Blade> query = cb.createQuery(Blade.class);
        Root<Blade> root = query.from(Blade.class);
        
        query.select(root);
        applyFilters(cb, query, root, filters);
        
        return entityManager.createQuery(query).getResultList();
    }

    public List<Rubber> searchRubbers(List<FilterDTO> filters) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rubber> query = cb.createQuery(Rubber.class);
        Root<Rubber> root = query.from(Rubber.class);
        
        query.select(root);
        applyFilters(cb, query, root, filters);
        
        return entityManager.createQuery(query).getResultList();
    }

    private <T> void applyFilters(CriteriaBuilder cb, CriteriaQuery<T> query, Root<T> root, List<FilterDTO> filters) {
        if (filters != null && !filters.isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            for (FilterDTO filter : filters) {
                Path<?> filterPath = resolvePath(root, filter.getField());
                predicates.add(buildPredicate(cb, filterPath, filter));
            }
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Path<T> resolvePath(Root<?> root, String field) {
        String[] parts = field.split("\\.");
        Path<?> currentPath = root;
        for (String part : parts) {
            currentPath = currentPath.get(part);
        }
        return (Path<T>) currentPath;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Predicate buildPredicate(CriteriaBuilder cb, Path path, FilterDTO filter) {
        String op = filter.getOperator().toUpperCase();
        Object value = filter.getValue();
        
        switch (op) {
            case "EQ":
                return cb.equal(path, value);
            case "GTE":
                if (value instanceof Number) {
                    return cb.ge(path, (Number) value);
                }
                return cb.greaterThanOrEqualTo(path, value.toString());
            case "LTE":
                if (value instanceof Number) {
                    return cb.le(path, (Number) value);
                }
                return cb.lessThanOrEqualTo(path, value.toString());
            case "LIKE":
                return cb.like(cb.lower(path), "%" + value.toString().toLowerCase() + "%");
            case "IN":
                CriteriaBuilder.In<Object> inClause = cb.in(path);
                if (value instanceof Iterable) {
                    for (Object val : (Iterable<?>) value) {
                        inClause.value(val);
                    }
                } else if (value instanceof Object[]) {
                    for (Object val : (Object[]) value) {
                        inClause.value(val);
                    }
                } else {
                    inClause.value(value);
                }
                return inClause;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + op);
        }
    }
}
