package com.epam.esm.model.util;

import lombok.NoArgsConstructor;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.model.repository.RepositoryConstant.*;

@NoArgsConstructor
public class QueryBuildHelper {


    private CriteriaBuilder criteriaBuilder;

    public QueryBuildHelper(CriteriaBuilder criteriaBuilder) {
        this.criteriaBuilder = criteriaBuilder;
    }

    public <T> Optional<T> getOptionalQueryResult(Query query) {
        try {
            T entity = (T) query.getSingleResult();
            return Optional.of(entity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public <T> List<Order> buildOrderList(Root<T> root, List<String> sortColumns, List<String> orderTypes) {
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < sortColumns.size(); i++) {
            String column = sortColumns.get(i);
            String orderType = orderTypes.size() > i ? orderTypes.get(i) : ASC;
            Order order = orderType.equalsIgnoreCase(ASC) ? criteriaBuilder.asc(root.get(column)) : criteriaBuilder.desc(root.get(column));
            orderList.add(order);
        }
        return orderList;
    }

    public <T> List<Predicate> buildFilterBy(Root<T> root, List<String> filterBy) {
        List<Predicate> predicateList = new ArrayList<>(filterBy.size());
        for (int i = 0; i < filterBy.size(); i++) {
            if (i == 0) {
                predicateList.add(getPredicate(root, filterBy.get(i), NAME));
            } else if (i == 1) {
                predicateList.add(getPredicate(root, filterBy.get(i), DESCRIPTION));
            }
        }
        return predicateList;
    }

    public <T> Predicate buildOrEqualPredicates(Path<T> root, String columnName, List<?> values) {
        Predicate predicate = null;
        for (int i = 0; i < values.size(); i++) {
            Predicate currentPredicate = criteriaBuilder.equal(root.get(columnName), values.get(i));
            predicate = i == 0 ? currentPredicate : criteriaBuilder.or(predicate, currentPredicate);
        }
        return predicate;
    }

    private <T> Predicate getPredicate(Root<T> root, String filterBy, String predicateName) {
        return criteriaBuilder.like(root.get(predicateName), filterBy.concat("%"));
    }
}
