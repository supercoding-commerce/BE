package com.github.commerce.web.dto.product;

import com.github.commerce.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SellingProductDto {

    @ApiModelProperty(value = "productId", example = "1")
    private Long productId;

    @ApiModelProperty(value = "shopName", example = "oo샵")
    private String shopName;

    @ApiModelProperty(value = "name", example = "상품 이름")
    private String name;

    @ApiModelProperty(value = "content", example = "상품 내용")
    private String content;

    @ApiModelProperty(value = "price", example = "10000")
    private Integer price;

    @ApiModelProperty(value = "leftAmount", example = "(재고 수량) 1")
    private Integer leftAmount;

    @ApiModelProperty(value = "thumbnailUrl", example = "썸네일 이미지 주소")
    private String thumbnailUrl;

    public SellingProductDto (Product product){
        this.productId = product.getId();
        this.shopName = product.getSeller().getShopName();
        this.name = product.getName();
        this.content = product.getContent();
        this.price = product.getPrice();
        this.leftAmount = product.getLeftAmount();
        this.thumbnailUrl = product.getThumbnailUrl();
    }

}
