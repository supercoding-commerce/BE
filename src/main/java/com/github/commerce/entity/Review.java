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
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User users;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id", nullable = false)
    private Product products;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orders_id", nullable = false)
    private Order orders;

    @Column(name = "author", length = 100)
    private String author;

    @Column(name = "content", length = 200)
    private String content;

    @Column(name = "star_point", nullable = false, columnDefinition = "int default 0")
    private Short starPoint;

    @Column(name = "is_deleted", columnDefinition = "tinyint default 0")
    private Boolean isDeleted;

    @Column(name = "image_url", length = 200)
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;



}