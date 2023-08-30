package com.github.commerce.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long reviewId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "payment_historys_id")
//    private PaymentHistory paymentHistory;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    private Product product;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "star", nullable = false, columnDefinition = "int default 0")
    private Integer star;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted",nullable = false, columnDefinition = "tinyint(1) default 0")
    private Boolean isDeleted;


//    public static Review toEntity(User user, Product product, CreateReview.Request request) {
//        return Review.builder()
//                .user(user)
//                .author(user.getNickname())
//                .product(product)
//                .title(request.getTitle())
//                .content(request.getContent())
//                .createAt(LocalDateTime.now())
//                .isDeleted(false)
//                .build();
//    }
}