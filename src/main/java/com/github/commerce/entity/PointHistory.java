package com.github.commerce.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "point_histories")
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pay_money_id", nullable = false)
    private PayMoney payMoney;

    @Column(name = "earned_point")
    private Long earnedPoint;

    @Column(name = "used_point")
    private Long usedPoint;

    @Column(name = "create_at")
    LocalDateTime createAt;

    @Column(name = "status")
    private Integer status;
}
