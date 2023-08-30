package com.github.commerce.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "users_id", nullable = false)
    private User users;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "products_id", nullable = false)
    private Product products;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_histories_id", nullable = false)
    private PaymentHistory paymentHistories;

    @Size(max = 200)
    @Column(name = "content", length = 200)
    private String content;

    @Column(name = "star_point")
    private Short starPoint;

    @Column(name = "is_deleted")
    private Byte isDeleted;

    @Column(name = "created_at")
    private Instant createdAt;

    @NotNull
    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Size(max = 255)
    @Column(name = "nickname")
    private String nickname;

    @NotNull
    @Column(name = "star", nullable = false)
    private Integer star;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

}