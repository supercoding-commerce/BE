package com.github.commerce.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id")
    private Product products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User users;

    @Column(name = "quantity")
    private Integer quantity;

    @Builder.Default
    @Column(name = "is_ordered", columnDefinition = "tinyint default 0")
    private Boolean isOrdered = false;

    @Column(name = "order_tag")
    private String orderTag;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder.Default
    @Column(name = "cart_state", columnDefinition = "int default 0")
    private Integer cartState = 0;

    @Column(name = "failed_causes")
    private String failed_causes;

    @Builder.Default
    @Column(name = "options")
    private String options = "[]";

}