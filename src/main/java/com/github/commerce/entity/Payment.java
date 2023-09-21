package com.github.commerce.entity;

import com.github.commerce.web.dto.payment.PaymentDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_money_id")
    private PayMoney payMoney;

    @Column(name = "payment_method")
    private Integer paymentMethod;

    @CreatedDate
    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    private Integer status;

    @Column(name = "pay_money_amount")
    private Long payMoneyAmount;

    public static Payment payment(Payment payment){
        return Payment.builder()
                .payMoney(payment.getPayMoney())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .build();
    }


    public static Payment fromDto(PaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setPaymentMethod(Integer.valueOf(paymentDto.getPaymentMethod()));
        payment.setPayMoneyAmount(paymentDto.getPayMoneyAmount());

        return payment;
    }


}