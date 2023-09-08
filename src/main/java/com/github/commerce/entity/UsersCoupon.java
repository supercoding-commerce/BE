package com.github.commerce.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users_coupons")
public class UsersCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupons_id", nullable = false)
    private Coupon coupons;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User users;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Builder.Default
    @Column(name = "is_used")
    private Boolean isUsed = false;

    public UsersCoupon (Coupon coupon, User user, LocalDateTime expiredAt){
        this.coupons = coupon;
        this.users = user;
        this.expiredAt = expiredAt;
        this.isUsed = false;
    }

}