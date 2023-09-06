package com.github.commerce.web.controller.product;

import com.github.commerce.entity.collection.ProductOption;
import com.github.commerce.service.product.ProductService;
import com.github.commerce.web.dto.product.ProductRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/product")
@Api(tags = "상품 CRUD API")
public class ProductController {

    private final ProductService productService;

    @GetMapping("v1/api/mongo/{productId}")
    public ResponseEntity<ProductOption> getMongo(
            @PathVariable() int productId
    ) {
        return ResponseEntity.ok(productService.getMongo(productId));
    }
    // 상품 등록
    @ApiOperation(value = "상품 등록")
    @PostMapping(value = "/", consumes = "multipart/form-data")
    public ResponseEntity<?> createProduct(@RequestPart ProductRequest productRequest,
                                           @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
                                           @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) {
        productService.createProductItem(productRequest,thumbnailImage,imageFiles);
        return ResponseEntity.ok("상품 등록 완료");
    }
}
