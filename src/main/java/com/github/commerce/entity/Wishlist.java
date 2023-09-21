package com.github.commerce.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "wishlists")
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User users;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id")
    private Product products;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}