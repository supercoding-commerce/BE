package com.github.commerce.web.dto.payment;

import com.github.commerce.entity.PointHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointHistoryDto {

    @ApiModelProperty(value = "유저 지갑 식별값")
    private Long payMoneyId;

    @ApiModelProperty(value = "적립 포인트")
    private Long earnedPoint;

    @ApiModelProperty(value = "사용 포인트")
    private Long usedPoint;

    @ApiModelProperty(value = "적립 날짜")
    private LocalDateTime createdAt;

    // 적립 | 사용 선택
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "상태")
    private String status;

    public static PointHistoryDto fromEntity(PointHistory pointHistory){
        return PointHistoryDto.builder()
                .payMoneyId(pointHistory.getPayMoney().getId())
                .earnedPoint(pointHistory.getEarnedPoint())
                .usedPoint(pointHistory.getUsedPoint())
                .createdAt(pointHistory.getCreatedAt())
                .status(PointStatusEnum.getByCode(pointHistory.getStatus()))
                .build();
    }


}
