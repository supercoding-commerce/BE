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
@Table(name = "charge_histories")
public class ChargeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(mappedBy = "chargeHistories", cascade = CascadeType.ALL, orphanRemoval = true)
    private PayMoney payMoneyId;

    @Column(name = "pay_money")
    private Long payMoney;

    @Column(name = "payment_amount")
    private Long paymentAmount;

    @CreatedDate
    @Column(name = "charge_date")
    private LocalDateTime chargeDate;

    @Column(name = "status")
    private Integer status;

}