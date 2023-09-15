package com.github.commerce.web.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.commerce.entity.Seller;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProductDto {
    private Long productId;

    private String shopName;

    private String name;

    private Integer price;

    private Integer leftAmount;

    private String productCategory;

    private String ageCategory;

    private String genderCategory;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private List<String> imageUrl;


    public GetProductDto(Long id, String name, Integer price, LocalDateTime createdAt, String productCategory, String ageCategory, String genderCategory, Integer leftAmount, String thumbnailUrl, String shopName) {
        this.productId = id;
        this.name = name;
        this.price = price;
        this.createdAt = createdAt;
        this.productCategory = productCategory;
        this.ageCategory = ageCategory;
        this.genderCategory = genderCategory;
        this.leftAmount = leftAmount;
        this.imageUrl = convertUrlList(thumbnailUrl);
        this.shopName = shopName;
    }


    private static List<String> convertUrlList(String urlList){
        if (urlList != null && !urlList.isEmpty()) {
            return Arrays.asList(urlList.split(","));
        } else {
            return Collections.emptyList(); // 빈 리스트 반환
        }
    }
}