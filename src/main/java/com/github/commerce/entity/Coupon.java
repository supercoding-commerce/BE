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
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Size(max = 255)
    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Instant createdAt;

    @NotNull
    @Column(name = "period", nullable = false)
    private Integer period;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Size(max = 255)
    @Column(name = "coupon_grade")
    private String couponGrade;

    @Column(name = "coupon_amount")
    private Integer couponAmount;

}