package com.github.commerce.entity;

import com.github.commerce.web.dto.product.ProductRequest;
import com.google.gson.Gson;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellers_id", nullable = false)
    private Seller seller;

    @NotNull
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Size(max = 255)
    @Column(name = "content")
    private String content;

    @Size(max = 255)
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "left_amount")
    private Integer leftAmount;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotNull
    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    private Boolean isDeleted ;

    @Column(name = "product_category", length = 20)
    private String productCategory;

    @Column(name = "gender_category", length = 10)
    private String genderCategory;

    @Column(name = "age_category", length = 10)
    private String ageCategory;

    @Column(name = "options")
    private String options;

    public static Product from(Product originProduct, ProductRequest productRequest) {
        List<String> options = productRequest.getOptions();
        Gson gson = new Gson();
        String inputOptionsJson = gson.toJson(options);
        return Product.builder()
                .id(originProduct.getId())
                .options(inputOptionsJson)
                .name(productRequest.getName())
                .seller(originProduct.getSeller())
                .price(productRequest.getPrice())
                .content(productRequest.getContent())
                .leftAmount(productRequest.getLeftAmount())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .productCategory(productRequest.getProductCategory())
                .ageCategory(productRequest.getAgeCategory())
                .genderCategory(productRequest.getGenderCategory())
                .build();
    }


}