package com.github.commerce.web.controller.cart;

import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.cart.CartService;
import com.github.commerce.web.dto.cart.CartDto;
import com.github.commerce.web.dto.cart.GetCartDto;
import com.github.commerce.web.dto.cart.PostCartDto;
import com.github.commerce.web.dto.cart.PutCartDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "장바구니 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/cart")
@RestController
public class CartController {
    private final CartService cartService;


    /**
     * 장바구니 전체조회
     * @param cursorId
     * @return
     */
    @ApiOperation(value = "장바구니 조회, 로그인필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = List.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping
    public ResponseEntity getAllCartWithCursor(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false) Long cursorId
    ){
        Long userId = userDetails.getUser().getId();

        if(cursorId == null){
            return ResponseEntity.ok(
                            cartService.getAllCarts(userId)
            );
        }else{
            return ResponseEntity.ok(
                    GetCartDto.Response.fromPage(
                            cartService.getAllCartWithCursor(userId, cursorId)
                    )
            );
        }
    }

    /**
     * 장바구니 상품추가
     * @param request
     * @return
     */
    @ApiOperation(value = "장바구니 생성, 로그인필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping
    public ResponseEntity<String> add(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PostCartDto.Request request
            ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(
                //PostCartDto.Response.from(cartService.addToCart(request, userId))
                cartService.addToCart(request, userId)
        );
    }

    /**
     * 장바구니 상품수정
     * @param request
     * @return
     */
    @ApiOperation(value = "장바구니 수정, 로그인필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PutMapping
    public ResponseEntity<String> modify(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PutCartDto.Request request
            ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(cartService.modifyCart(request, userId));
    }

    /**
     * 장바구니 전체삭제
     * @return
     */
    @ApiOperation(value = "장바구니 전체삭제, 로그인필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @DeleteMapping
    public ResponseEntity<String> deleteAll(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(cartService.deleteAll(userId));
    }

    /**
     * 장바구니 개별삭제
     * @return
     */
    @ApiOperation(value = "장바구니 개별삭제, 로그인필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteOne(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cartId
    ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(cartService.deleteOne(cartId, userId));
    }

}
