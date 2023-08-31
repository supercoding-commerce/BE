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
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "expired_at")
    private Instant expiredAt;

    @Lob
    @Column(name = "event_image")
    private String eventImage;

    @Size(max = 255)
    @Column(name = "event_grade")
    private String eventGrade;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "is_popup")
    private Boolean isPopup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupons_id")
    private Coupon coupons;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellers_id")
    private Seller sellers;

}