package com.github.commerce.entity;

import com.github.commerce.web.dto.product.ProductRequest;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sellers_id", nullable = false)
    private Seller seller;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content")
    private String content;

    @Size(max = 255)
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @NotNull
    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "left_amount")
    private Long leftAmount;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotNull
    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    private Boolean isDeleted ;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "gender_category")
    private String genderCategory;

    @Column(name = "age_category")
    private String ageCategory;


    public static Product from(Product originProduct, ProductRequest productRequest) {
        return Product.builder()
                .id(originProduct.getId())
                .name(productRequest.getName())
                .seller(originProduct.getSeller())
                .price(productRequest.getPrice())
                .content(productRequest.getContent())
                .leftAmount(productRequest.getLeftAmount())
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .productCategory("test")
                .ageCategory("test")
                .genderCategory("test")
                .build();
    }
}