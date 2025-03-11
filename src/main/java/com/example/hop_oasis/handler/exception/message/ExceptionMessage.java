package com.example.hop_oasis.handler.exception.message;

public record ExceptionMessage() {
    public static final String RESOURCE_NOT_FOUND = "Resource [%s] not found";
    public static final String RESOURCE_DELETED = "Resource [%s] is deleted";
    public static final String ORDER_DELETED = "Order with ID [%s] has already been deleted or does not exist";

}
