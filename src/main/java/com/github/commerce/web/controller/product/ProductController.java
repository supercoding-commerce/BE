package com.github.commerce.web.controller.product;

import com.github.commerce.entity.collection.ProductOption;
import com.github.commerce.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("v1/api/mongo/{productId}")
    public ResponseEntity<ProductOption> getMongo(
            @PathVariable() int productId
    ) {
        return ResponseEntity.ok(productService.getMongo(productId));
    }


    /**
     * ALB 헬스체크 API
     */
    @GetMapping("v1/api/navi")
    public ResponseEntity<String> getHealthCheck(){
        return ResponseEntity.ok("안녕! 물고기는 고마웠어요!");
    }
}
