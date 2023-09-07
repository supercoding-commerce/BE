package com.github.commerce.web.dto.payment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointHistoryDto {

    private Long payMoneyId;

    private Long earnedPoint;

    private Long usedPoint;

    private LocalDateTime createAt;

    private Integer status;
}
