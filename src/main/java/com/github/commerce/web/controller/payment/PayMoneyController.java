package com.github.commerce.web.controller.payment;

import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.payment.PayMoneyService;
import com.github.commerce.web.dto.payment.GetPayMoneyDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "페이머니 API")
@RequiredArgsConstructor
@RequestMapping("/v1/api/pay-moneys")
@RestController
public class PayMoneyController {

    final private PayMoneyService payMoneyService;

    @ApiOperation(value = "페이머니조회")
    @GetMapping("/list")
    public ResponseEntity<List<GetPayMoneyDto.GetPayMoneyResponse>> getPayMoneys(
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUser().getId();
        List<GetPayMoneyDto.GetPayMoneyResponse> payMoneyList =
                payMoneyService.getPayMoneyList(userId);
        return ResponseEntity.ok(payMoneyList);
    }


}