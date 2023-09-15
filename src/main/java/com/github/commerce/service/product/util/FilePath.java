package com.github.commerce.service.product.util;

public enum FilePath {
    REVIEW_IMG_DIR("review/"),
    SHOP_IMG_DIR("seller/"),
    PRODUCT_THUMB_NAIL_DIR("product/thumbnails/"),
    PRODUCT_CONTENT_DIR("product/content/"),
    SEPARATE_POINT(".com/");

    private final String path;

    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
