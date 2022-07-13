package com.epam.esm.model.repository;

import com.epam.esm.model.entity.ApplicationBaseEntity;
import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.util.QueryBuildHelper;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.Optional;

import static com.epam.esm.model.repository.RepositoryConstant.*;

@Transactional
public abstract class AbstractRepository<T extends ApplicationBaseEntity> implements EntityRepository<T> {


    @PersistenceContext
    protected final EntityManager entityManager;
    protected final CriteriaBuilder criteriaBuilder;
    protected final Class<T> entityType;
    protected final QueryBuildHelper criteriaBuilderHelper;

    public AbstractRepository(EntityManager entityManager, Class<T> entityType) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.entityType = entityType;
        this.criteriaBuilderHelper = new QueryBuildHelper(this.criteriaBuilder);
    }

    @Override
    public T create(T entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        }
        return entityManager.merge(entity);
    }

    @Override
    public CustomPage<T> getAll(Pageable pageable) {
        CriteriaQuery<T> query = criteriaBuilder.createQuery(entityType);
        Root<T> root = query.from(entityType);
        query.select(root);
        int totalAmount = getTotalAmount();
        return CustomPage.<T>builder()
                .content(entityManager.createQuery(query)
                        .setFirstResult((int) pageable.getOffset())
                        .setMaxResults(pageable.getPageSize())
                        .getResultList())
                .amountOfPages(totalAmount / pageable.getPageSize())
                .currentPage(pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber())
                .firstPage(1)
                .lastPage(totalAmount / pageable.getPageSize())
                .pageSize(pageable.getPageSize())
                .build();
    }

    @Override
    public Optional<T> getByField(String fieldName, Object value) {
        CriteriaQuery<T> query = criteriaBuilder.createQuery(entityType);
        Root<T> root = query.from(entityType);
        query.select(root);
        Predicate fieldPredicate = criteriaBuilder.equal(root.get(fieldName), value);
        query.where(fieldPredicate);
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        return criteriaBuilderHelper.getOptionalQueryResult(typedQuery);
    }

    @Override
    public T update(T entity) {
        return create(entity);
    }

    @Override
    public void delete(T entity) {
        T mergedEntity = entityManager.merge(entity);
        entityManager.remove(mergedEntity);
    }

    private int getTotalAmount() {
        return switch (entityType.getSimpleName()) {
            case USER_CLASS_SIMPLE_NAME -> ((BigInteger) entityManager.createNativeQuery(SELECT_COUNT_FROM_USER).getSingleResult()).intValue();
            case GIFTCERTIFICATE_CLASS_SIMPLE_NAME -> ((BigInteger) entityManager.createNativeQuery(SELECT_COUNT_FROM_GIFT_CERTIFICATE).getSingleResult()).intValue();
            case TAG_CLASS_SIMPLE_NAME -> ((BigInteger) entityManager.createNativeQuery(SELECT_COUNT_FROM_TAG).getSingleResult()).intValue();
            case ORDER_CLASS_SIMPLE_NAME -> ((BigInteger) entityManager.createNativeQuery(SELECT_COUNT_FROM_ORDERS).getSingleResult()).intValue();
            default -> 0;
        };
    }
}
