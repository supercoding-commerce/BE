package com.github.commerce.web.controller.product;

import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.entity.Wishlist;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.product.WishlistService;
import com.github.commerce.service.product.exception.ProductErrorCode;
import com.github.commerce.service.product.exception.ProductException;
import com.github.commerce.service.product.util.ValidateProductMethod;
import com.github.commerce.service.user.exception.UserErrorCode;
import com.github.commerce.service.user.exception.UserException;
import com.github.commerce.web.advice.custom.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/wishlist")
@Api(tags = "찜 목록 API")
public class WishListController {
    private final WishlistService wishlistService;
    private final ValidateProductMethod validateProductMethod;
    private final ProductRepository productRepository;

    @PostMapping("/add")
    @ApiOperation(value = "찜 등록", notes = "찜 목록에 상품을 추가 합니다.")
    public ResponseEntity<String> addWishlist(@RequestParam(value = "productId",required = false) Long productId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {throw new UserException(UserErrorCode.UER_NOT_FOUND);}
        User profileId = validateProductMethod.validateUser(userDetails.getUser().getId());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT));
        try {
            boolean addedToWishlist = wishlistService.addWishlist(profileId, product);
            if (addedToWishlist) {
                return ResponseEntity.ok("찜 목록에 추가되었습니다.");
            } else {
                return ResponseEntity.ok("찜 목록에 상품이 포함되어 있습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.ok("판매자는 상품 등록 및 삭제를 할 수 없습니다.");
        }
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "찜 삭제", notes = "찜 목록에서 상품을 삭제합니다.")
    public ResponseEntity<String> removeWishlist(@RequestParam(value = "productId" , required = false) Long productId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {throw new UserException(UserErrorCode.UER_NOT_FOUND);}
        User profileId = validateProductMethod.validateUser(userDetails.getUser().getId());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT));
        try {
            boolean removedFromWishlist = wishlistService.removeWishlist(profileId, product);
            if (removedFromWishlist) {
                return ResponseEntity.ok("찜 목록에서 제거되었습니다.");
            } else {
                return ResponseEntity.ok("찜 목록에 포함되지 않은 상품입니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.ok("판매자는 찜 등록 및 삭제를 할 수 없습니다.");
        }
    }

    @GetMapping
    @ApiOperation(value = "찜 목록 조회",notes = "찜 목록을 조회합니다.")
    public ResponseDto<String> getWishlist(
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        User profileId = validateProductMethod.validateUser(userDetails.getUser().getId());
        String getProducts = wishlistService.getWishlist(profileId);
        return ResponseDto.success(getProducts +"성공~!");
    }

}