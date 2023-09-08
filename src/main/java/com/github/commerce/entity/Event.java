package com.github.commerce.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

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

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

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