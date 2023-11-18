package com.github.commerce.entity;

import com.github.commerce.web.dto.product.GetProductDto;
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
@SqlResultSetMapping(
        name = "GetProductDtoMapping",
        classes = @ConstructorResult(
                targetClass = GetProductDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "price", type = Integer.class),
                        @ColumnResult(name = "created_at", type = LocalDateTime.class),
                        @ColumnResult(name = "product_category", type = String.class),
                        @ColumnResult(name = "age_category", type = String.class),
                        @ColumnResult(name = "gender_category", type = String.class),
                        @ColumnResult(name = "left_amount", type = Integer.class),
                        @ColumnResult(name = "thumbnail_url", type = String.class),
                        @ColumnResult(name = "shop_name", type = String.class)
                }
        )
)
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
    @Column(name = "name", nullable = false, length = 100)
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
}