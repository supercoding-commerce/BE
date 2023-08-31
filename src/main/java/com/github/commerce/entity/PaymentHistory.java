package com.github.commerce.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment_histories")
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "charge_histories_id", nullable = false)
    private ChargeHistory chargeHistories;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payments_id", nullable = false)
    private Payment payments;

    @Column(name = "payment_amount")
    private Long paymentAmount;

    @Column(name = "payment_date")
    private Instant paymentDate;

    @Size(max = 50)
    @Column(name = "payment_status", length = 50)
    private String paymentStatus;

    @Column(name = "point", precision = 10, scale = 2)
    private BigDecimal point;

    @Column(name = "pay_money")
    private Long payMoney;

}