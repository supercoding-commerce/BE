package com.github.commerce.web.controller.product;

import com.github.commerce.entity.collection.ProductOption;
import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.product.ProductService;
import com.github.commerce.web.dto.product.ProductRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // 상품 등록
    @ApiOperation(value = "상품 등록")
    @PostMapping("/test")
    public ResponseEntity<String> createProduct(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value="productRequest") ProductRequest productRequest,
                                           @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
                                           @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) {
        System.out.printf("1111111" + productRequest.getName());
        productService.createProductItem(productRequest,thumbnailImage,imageFiles, userDetails.getUser().getId());
        return ResponseEntity.ok("상품 등록 완료");
    }
}
