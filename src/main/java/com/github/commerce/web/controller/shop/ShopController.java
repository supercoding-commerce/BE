package com.github.commerce.web.controller.shop;

import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.shop.ShopService;
import com.github.commerce.web.dto.product.SellingProductDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/product/seller")
@Api(tags = "판매중인 상품 조회 API")
public class ShopController {

    private final ShopService shopService;

    @ApiOperation(value = "판매자의 판매중인 내역 조회, 로그인 필요")
    @GetMapping
    public ResponseEntity<List<SellingProductDto>> getSellingProducts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(shopService.getSellingProducts(userId));
    }

}
