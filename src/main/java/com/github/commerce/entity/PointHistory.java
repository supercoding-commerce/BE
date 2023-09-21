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
@Table(name = "point_histories")
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private PayMoney payMoney;

    @Column(name = "earned_point")
    private Long earnedPoint;

    @Column(name = "used_point")
    private Long usedPoint;

    @CreatedDate
    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    private Integer status;
}
