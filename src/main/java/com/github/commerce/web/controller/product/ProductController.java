package com.github.commerce.web.controller.product;

import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.product.ProductService;
import com.github.commerce.web.dto.product.GetProductDto;
import com.github.commerce.web.dto.product.ProductCategoryEnum;
import com.github.commerce.web.dto.product.ProductDto;
import com.github.commerce.web.dto.product.ProductRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @ApiOperation(value = "상품 검색")
    @GetMapping("/search") //  ?pageNumber=1&searchWord=반바지
    public ResponseEntity<List<GetProductDto>> searchProduct(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(name = "searchWord", required = false, defaultValue = "") String searchWord,
            @RequestParam(name = "ageCategory", required = false, defaultValue = "") String ageCategory,
            @RequestParam(name = "genderCategory", required = false, defaultValue = "") String genderCategory,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy
    ){
        return ResponseEntity.ok(productService.searchProducts(pageNumber, searchWord, ageCategory,genderCategory, sortBy));
    }

    @ApiOperation(value = "메인페이지 무한스크롤")
    @GetMapping
    public ResponseEntity<List<GetProductDto>> getProducts(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(name = "ageCategory", required = false, defaultValue = "") String ageCategory,
            @RequestParam(name = "genderCategory", required = false, defaultValue = "") String genderCategory,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy
    )
    {
        return ResponseEntity.ok(productService.getProductList(pageNumber,ageCategory,genderCategory, sortBy));
    }

    @ApiOperation(value = "상품 카테고리별 조회")
    @GetMapping("/category/{productCategory}")
    public ResponseEntity<List<GetProductDto>> getProductsByCategory(

            @PathVariable String productCategory, //필수
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(name = "ageCategory", required = false, defaultValue = "") String ageCategory,
            @RequestParam(name = "genderCategory", required = false, defaultValue = "") String genderCategory,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy
    )
    {
        return ResponseEntity.ok(productService.getProductsByCategory(pageNumber, productCategory,ageCategory,genderCategory, sortBy));
    }

    @ApiOperation(value = "상품 상세 조회")
    @GetMapping("/detail/{productId}")
    public ResponseEntity<ProductDto> getProduct(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long productId
    ){
        Long userId = userDetails != null ? userDetails.getUser().getId() : null;
        return ResponseEntity.ok(productService.getOneProduct(productId, userId));
    }

    // 판매자가 상품 등록
    @ApiOperation(value = "상품 등록")
    @PostMapping
    public ResponseEntity<String> createProduct(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart ProductRequest productRequest,
            @RequestPart(required = false) List<MultipartFile> imageFiles) {
        Long profileId = (userDetails != null) ? userDetails.getUser().getId() : null;

        return ResponseEntity.ok(productService.createProductItem(productRequest, imageFiles, profileId));
    }

    // 상품 수정
    @ApiOperation(value = "상품 식별값을 입력하여 단일의 product 레코드를 수정합니다.")
    @PatchMapping(value = "/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") Long productId,
                                           @ModelAttribute ProductRequest productRequest,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails
                                           ) {
        Long profileId = (userDetails != null) ? userDetails.getUser().getId() : null;
        productService.updateProductById(productId,profileId,productRequest);

        return ResponseEntity.ok(productId + "번 상품 수정 성공");
    }



    // 상품 삭제
    @ApiOperation(value="상품 식별값을 입력하여 단일의 product 레코드를 삭제합니다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("productId") Long productId){
        Long profileId = (userDetails != null) ? userDetails.getUser().getId() : null;
        productService.deleteProductByProductId(productId,profileId);
        return ResponseEntity.ok(productId + "번 상품 삭제 성공");
    }

}
