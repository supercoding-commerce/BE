package com.github.commerce.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_content_images")
public class ProductContentImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @Lob
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    public static ProductContentImage from(Product product, String imageUrl) {
        return ProductContentImage.builder()
                .product(product)
                .imageUrl(imageUrl)
                .build();
    }
}
