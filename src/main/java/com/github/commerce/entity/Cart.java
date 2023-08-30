package com.github.commerce.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", nullable = false)
    private Long cartId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "order_state")
    private Integer orderState;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //MongoDB objectId
    @Column(name = "option_id")
    private String optionId;
}
