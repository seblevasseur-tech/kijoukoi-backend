package com.kijoukoi.app.infrastructure;

import com.kijoukoi.app.domain.Player;
import com.kijoukoi.app.domain.dto.AggregationRequestDTO;
import com.kijoukoi.app.domain.dto.AggregationResultDTO;
import com.kijoukoi.app.domain.dto.FilterDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DynamicStatsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<AggregationResultDTO> aggregate(AggregationRequestDTO request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AggregationResultDTO> query = cb.createQuery(AggregationResultDTO.class);
        Root<Player> root = query.from(Player.class);

        // Resolve GroupBy Path
        Path<String> groupPath = resolvePath(root, request.getGroupBy());

        // Select clause
        Expression<Long> countExp = cb.count(root);
        query.select(cb.construct(AggregationResultDTO.class, groupPath, countExp));

        // Filters
        if (request.getFilters() != null && !request.getFilters().isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            for (FilterDTO filter : request.getFilters()) {
                Path<?> filterPath = resolvePath(root, filter.getField());
                predicates.add(buildPredicate(cb, filterPath, filter));
            }
            // To ensure we don't group null values if they shouldn't be counted:
            predicates.add(cb.isNotNull(groupPath));
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        } else {
            query.where(cb.isNotNull(groupPath));
        }

        // Group By
        query.groupBy(groupPath);

        // Order By Count Desc
        query.orderBy(cb.desc(countExp));

        return entityManager.createQuery(query).getResultList();
    }

    @SuppressWarnings("unchecked")
    private <T> Path<T> resolvePath(Root<Player> root, String field) {
        String[] parts = field.split("\\.");
        From<?, ?> currentFrom = root;
        Path<?> currentPath = root;
        
        for (String part : parts) {
            // Special case for our ManyToMany collection
            if ("tags".equals(part)) {
                currentFrom = currentFrom.join("tags", JoinType.LEFT);
                currentPath = currentFrom;
            } else {
                currentPath = currentPath.get(part);
            }
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
                return cb.like(path, "%" + value + "%");
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


    public List<Player> searchPlayers(AggregationRequestDTO request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Player> query = cb.createQuery(Player.class);
        Root<Player> root = query.from(Player.class);

        query.select(root);

        if (request.getFilters() != null && !request.getFilters().isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            for (FilterDTO filter : request.getFilters()) {
                Path<?> filterPath = resolvePath(root, filter.getField());
                predicates.add(buildPredicate(cb, filterPath, filter));
            }
            // Optional: If we want to ensure the groupBy field is also not null (like in aggregate)
            // But usually for search, we just apply the exact filters provided.
            if (request.getGroupBy() != null && !request.getGroupBy().isEmpty()) {
                 Path<?> groupPath = resolvePath(root, request.getGroupBy());
                 predicates.add(cb.isNotNull(groupPath));
            }
            
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        } else {
             if (request.getGroupBy() != null && !request.getGroupBy().isEmpty()) {
                 Path<?> groupPath = resolvePath(root, request.getGroupBy());
                 query.where(cb.isNotNull(groupPath));
            }
        }

        return entityManager.createQuery(query).getResultList();
    }
}