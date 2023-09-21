package com.github.commerce.entity;

import com.github.commerce.service.payment.exception.PaymentErrorCode;
import com.github.commerce.service.payment.exception.PaymentException;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 255)
    @Column(name = "password", length = 100)
    private String password;

    @Size(max = 100)
    @NotNull
    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @Size(max = 255)
    @Column(name = "telephone")
    private String telephone;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(name = "is_delete")
    private Boolean isDelete=false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsersCoupon> userCoupons;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private UsersInfo usersInfo;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<PayMoney> payMoney;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Seller seller;

    public PayMoney getPayMoneyByUserId(){
        return payMoney.stream().max(Comparator.comparing(PayMoney::getId)).orElseGet(PayMoney::new);
    }



}