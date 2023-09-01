package com.github.commerce.entity;

import com.github.commerce.web.dto.coupon.CouponRegisterRequest;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class) //@CreatedAt 위해 필요
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "title", unique = true)
    private String title;

    @Size(max = 255)
    @Column(name = "content")
    private String content;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "period")
    private Integer period;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;


    @Column(name = "coupon_grade")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Grade couponGrade = Grade.ALL;

    @Column(name = "coupon_amount")
    private Integer couponAmount;

    public Coupon(CouponRegisterRequest couponRegisterRequest){
        this.title = couponRegisterRequest.getTitle();
        this.content = couponRegisterRequest.getContent();
        this.period = couponRegisterRequest.getPeriod();
        this.couponGrade = Grade.valueOf(couponRegisterRequest.getCouponGrade());
        this.couponAmount = couponRegisterRequest.getCouponAmount();
    }

}