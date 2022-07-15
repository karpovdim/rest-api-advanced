package com.epam.esm.model.repository;

public class RepositoryConstant {
    private RepositoryConstant() {
    }

    public static final String ID = "id";
    public static final String ASC = "ASC";
    public static final String NAME = "name";
    public static final String USER = "user";
    public static final String TAGS = "tags";
    public static final String USER_ID = "userId";
    public static final String DESCRIPTION = "description";
    public static final String BEST_TAG_MAPPING_NAME = "BestTagMapping";

    protected static final String SELECT_COUNT_FROM_TAG = "SELECT COUNT(*) FROM tag";
    protected static final String SELECT_COUNT_FROM_USER = "SELECT COUNT(*) FROM user";
    protected static final String SELECT_COUNT_FROM_ORDERS = "SELECT COUNT(*) FROM orders";
    protected static final String SELECT_COUNT_FROM_GIFT_CERTIFICATE = "SELECT COUNT(*) FROM gift_certificate";

    protected static final String TAG_CLASS_SIMPLE_NAME = "Tag";
    protected static final String USER_CLASS_SIMPLE_NAME = "User";
    protected static final String ORDER_CLASS_SIMPLE_NAME = "Order";
    protected static final String GIFTCERTIFICATE_CLASS_SIMPLE_NAME = "GiftCertificate";
}
