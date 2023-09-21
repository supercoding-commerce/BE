package com.github.commerce.web.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.Seller;
import com.github.commerce.web.dto.order.DetailPageOrderDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long productId;

    private Long sellerId;

    private Long enteredUserId;

    private String enteredUserName;

    private String shopName;

    private String name;

    private String content;

    private Integer price;

    private Integer leftAmount;

    private String productCategory;

    private String ageCategory;

    private String genderCategory;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private String thumbnailUrl;

    private List<String> imageUrls;

    private String options;

    private Double averageStarPoint;

    private boolean isSeller;

    private List<DetailPageOrderDto> orderList;

    private String shopImageUrl;


    public static ProductDto fromEntity(Product product,boolean isSeller, List<String> imageUrls){

        return ProductDto.builder()
                .productId(product.getId())
                .sellerId(product.getSeller().getId())
                .shopName(product.getSeller().getShopName())
                .name(product.getName())
                .content(product.getContent())
                .price(product.getPrice())
                .leftAmount(product.getLeftAmount())
                .productCategory(product.getProductCategory())
                .ageCategory(product.getAgeCategory())
                .genderCategory(product.getGenderCategory())
                .createdAt(product.getCreatedAt())
                .thumbnailUrl(product.getThumbnailUrl())
                .imageUrls(imageUrls)
                .options(product.getOptions())
                .isSeller(isSeller)
                .build();
    }


    public static ProductDto fromEntityDetail(Product product, boolean isSeller, List<DetailPageOrderDto> orderList,
                                              Long userId, String userName, List<String> imageUrls, Double averageStar
                                              ){
        Seller seller = product.getSeller();
        return ProductDto.builder()
                .productId(product.getId())
                .sellerId(seller.getId())
                .enteredUserId(userId)
                .enteredUserName(userName)
                .shopName(seller.getShopName())
                .name(product.getName())
                .content(product.getContent())
                .price(product.getPrice())
                .leftAmount(product.getLeftAmount())
                .productCategory(product.getProductCategory())
                .ageCategory(product.getAgeCategory())
                .genderCategory(product.getGenderCategory())
                .createdAt(product.getCreatedAt())
                .thumbnailUrl(product.getThumbnailUrl())
                .imageUrls(imageUrls)
                .shopImageUrl(seller.getShopImageUrl())
                .options(product.getOptions())
                .averageStarPoint(averageStar)
                .isSeller(isSeller)
                .orderList(orderList)
                .build();
    }

    private static List<String> convertUrlList(String urlList){
        return Arrays.asList(urlList.split(","));
    }

    public static ProductDto fromUpdateEntity(Product originProduct, boolean isSeller, List<String> newImageFiles) {
        return ProductDto.builder()
                .productId(originProduct.getId())
                .sellerId(originProduct.getSeller().getId())
                .shopName(originProduct.getSeller().getShopName())
                .name(originProduct.getName())
                .content(originProduct.getContent())
                .price(originProduct.getPrice())
                .leftAmount(originProduct.getLeftAmount())
                .productCategory(originProduct.getProductCategory())
                .ageCategory(originProduct.getAgeCategory())
                .genderCategory(originProduct.getGenderCategory())
                .updatedAt(originProduct.getUpdatedAt())
                .thumbnailUrl(originProduct.getThumbnailUrl())
                .imageUrls(newImageFiles)
                .options(originProduct.getOptions())
                .isSeller(isSeller)
                .build();
    }
}
