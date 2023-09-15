package com.github.commerce.web.controller.payment;

import com.github.commerce.service.payment.PayMoneyService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "페이머니 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/pay-moneys")
public class PayMoneyController {

    private final PayMoneyService payMoneyService;

}
