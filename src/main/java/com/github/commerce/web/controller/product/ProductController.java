package com.github.commerce.web.controller.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class ProductController {


    @GetMapping("v1/api/navi")
    public ResponseEntity<String> getHealthCheck(){
        return ResponseEntity.ok("안녕! 물고기는 고마웠어요!");
    }
}
