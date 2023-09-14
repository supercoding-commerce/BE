package com.github.commerce.web.controller.payment;

import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.payment.PaymentService;
import com.github.commerce.web.advice.custom.ResponseDto;
import com.github.commerce.web.dto.payment.PurchaseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "주문 결제 API")
@RequiredArgsConstructor
@RequestMapping("/v1/api/payments")
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @ApiOperation(value = "상품 주문 결제")
    @PostMapping("/purchase")
    public ResponseEntity<PurchaseDto.PurchaseResponse> purchase(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PurchaseDto.PurchaseRequest request){

        Long userId = userDetails.getUser().getId();

        return ResponseEntity.ok(PurchaseDto.PurchaseResponse.from(paymentService.purchaseOrder(userId,request)));

    }
}
