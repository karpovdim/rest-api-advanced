package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.AbstractRepository;
import com.epam.esm.model.repository.GiftCertificateRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

import static com.epam.esm.model.repository.RepositoryConstant.*;

@Repository
@Transactional
public class GiftCertificateRepositoryImpl extends AbstractRepository<GiftCertificate> implements GiftCertificateRepository {


    public GiftCertificateRepositoryImpl(EntityManager entityManager) {
        super(entityManager, GiftCertificate.class);
    }

    @Override
    public CustomPage<GiftCertificate> getAllWithSortingAndFiltering(List<String> sortColumns, List<String> orderType,
                                                                     List<String> filterBy, Pageable pageable) {
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        List<Order> orderList = criteriaBuilderHelper.buildOrderList(root, sortColumns, orderType);
        query.orderBy(orderList);
        List<Predicate> predicateList = criteriaBuilderHelper.buildFilterBy(root, filterBy);
        query.select(root).where(predicateList.toArray(new Predicate[0]));
        int totalAmount = entityManager.createQuery(query).getResultList().size();
        return getGiftCertificateCustomPage(pageable, query, totalAmount);
    }

    @Override
    public CustomPage<GiftCertificate> getAllByTagNames(List<String> tagNames, Pageable pageable) {
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        Join<GiftCertificate, Tag> joinTag = root.join(TAGS);
        Predicate predicate = criteriaBuilderHelper.buildOrEqualPredicates(joinTag, NAME, tagNames);
        query.where(criteriaBuilder.and(predicate));
        query.groupBy(root.get(ID));
        query.having(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.count(root), (long) tagNames.size()));
        int totalAmount = getTotalAmountForGetAllTagNames(tagNames);
        return getGiftCertificateCustomPage(pageable, query, totalAmount);
    }

    private int getTotalAmountForGetAllTagNames(List<String> tagNames) {
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(criteriaBuilder.count(root));
        Join<GiftCertificate, Tag> joinTag = root.join(TAGS);
        Predicate predicate = criteriaBuilderHelper.buildOrEqualPredicates(joinTag, NAME, tagNames);
        query.where(criteriaBuilder.and(predicate));
        query.having(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.count(root), (long) tagNames.size()));
        return entityManager.createQuery(query).getSingleResult().intValue();
    }

    private CustomPage<GiftCertificate> getGiftCertificateCustomPage(Pageable pageable, CriteriaQuery<GiftCertificate> query, int totalAmount) {
        return CustomPage.<GiftCertificate>builder()
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
}

