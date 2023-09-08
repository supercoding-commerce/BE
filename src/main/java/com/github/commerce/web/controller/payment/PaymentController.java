package com.github.commerce.web.controller.payment;

import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.payment.PaymentService;
import com.github.commerce.web.dto.payment.PurchaseDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "물품주문결제 API")
@RequiredArgsConstructor
@RequestMapping("/v1/api/payments")
@RestController
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/purchase")
    public ResponseEntity<PurchaseDto.Response> purchase(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PurchaseDto.Request2 request){

        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(PurchaseDto.Response.from(paymentService.purchaseResponse(userId,request)));

    }
}
