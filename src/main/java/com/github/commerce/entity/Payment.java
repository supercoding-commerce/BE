package com.github.commerce.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_money_id")
    private PayMoney payMoney;

    @Column(name = "payment_method")
    private Integer paymentMethod;

    @CreatedDate
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "status")
    private Integer status;

    public static Payment payment(Payment payment){
        return Payment.builder()
                .order(payment.getOrder())
                .payMoney(payment.getPayMoney())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .build();
    }
}