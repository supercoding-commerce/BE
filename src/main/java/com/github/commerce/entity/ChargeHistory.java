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
@Table(name = "charge_histories")
public class ChargeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payments_id", nullable = false)
    private Payment payments;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "users_id", nullable = false)
    private User users;

    @Column(name = "charge_amount")
    private Long chargeAmount;

    @Column(name = "charge_bonus")
    private Long chargeBonus;

    @Column(name = "total_charge_amount")
    private Long totalChargeAmount;

    @Column(name = "charge_date")
    private Instant chargeDate;

    @Size(max = 255)
    @Column(name = "pg_payment_id")
    private String pgPaymentId;

}