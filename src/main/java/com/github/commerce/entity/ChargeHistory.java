package com.github.commerce.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "pay_money")
    private Long payMoney;

    @Column(name = "payment_amount")
    private Long paymentAmount;

    @Column(name = "bonus_pay_money")
    private Long bonusPayMoney;

    @Column(name = "total_pay_money")
    private Long totalPayMoney;

    @CreatedDate
    @Column(name = "charge_date")
    private LocalDateTime chargeDate;

}