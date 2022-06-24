package com.epam.esm.model.repository;

public class RepositoryConstant {
    private RepositoryConstant() {
    }

    protected static final String SELECT_COUNT_FROM_TAG = "SELECT COUNT(*) FROM tag";
    protected static final String SELECT_COUNT_FROM_USER = "SELECT COUNT(*) FROM user";
    protected static final String SELECT_COUNT_FROM_ORDERS = "SELECT COUNT(*) FROM orders";
    protected static final String SELECT_COUNT_FROM_GIFT_CERTIFICATE = "SELECT COUNT(*) FROM gift_certificate";

    protected static final String TAG_CLASS_SIMPLE_NAME = "Tag";
    protected static final String USER_CLASS_SIMPLE_NAME = "User";
    protected static final String ORDER_CLASS_SIMPLE_NAME = "Order";
    protected static final String GIFTCERTIFICATE_CLASS_SIMPLE_NAME = "GiftCertificate";
}
