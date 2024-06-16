package com.example.hop_oasis.hendler.exception.message;

public record ExceptionMessage() {
    public static final String BEER_NOT_FOUND = "Beer [%s] not found";
    public static final String BEERS_NOT_FOUND = "Beers [%s] not found";
    public static final String BEER_UPDATE_EXCEPTION = "Beer [%s] can;t be updated";
    public static final String BEER_DELETED = "Beer [%s] is already deleted";

    public static final String IMAGE_NOT_FOUND = "Image [%s] not found";
    public static final String IMAGE_DELETED = "Image [%s] is already deleted";
    public static final String IMAGE_DECOMPRESS_EXCEPTION = "Image [%s] can't be decompressed";
    public static final String IMAGE_COMPRESS_EXCEPTION = "Image [%s] can't be compressed";

    public static final String SNACK_NOT_FOUND = "Snack [%s] not found";
    public static final String SNACKS_NOT_FOUND = "Snacks [%s] not found";
    public static final String SNACK_DELETED = "Snack [%s] is already deleted";

    public static final String PRODUCT_BUNDLE_NOT_FOUND = "Product bundle [%s] not found";
    public static final String PRODUCTS_BUNDLE_NOT_FOUND = "Products bundle [%s] not found";
    public static final String PRODUCT_BUNDLE_DELETED = "Product bundle [%s] is already deleted";

}
