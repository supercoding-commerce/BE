package com.github.commerce.web.controller.payment;

import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.payment.PointHistoryService;
import com.github.commerce.web.advice.custom.ResponseDto;
import com.github.commerce.web.dto.payment.PointHistoryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "포인트 내역 API")
@RestController
@RequestMapping("/v1/api/points")
@RequiredArgsConstructor
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;

    @GetMapping("/history")
    @ApiOperation("포인트 내역 조회")
    public ResponseEntity<ResponseDto<List<PointHistoryDto>>> getPointHistoryList(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        List<PointHistoryDto> pointHistoryDtoList = pointHistoryService.getPointHistoryList(userId);
        return ResponseEntity.ok(ResponseDto.success(pointHistoryDtoList));
    }
}
