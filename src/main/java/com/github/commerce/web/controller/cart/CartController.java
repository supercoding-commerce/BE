package com.github.commerce.web.controller.cart;

import com.github.commerce.service.cart.CartService;
import com.github.commerce.web.dto.cart.CartDto;
import com.github.commerce.web.dto.cart.GetCartDto;
import com.github.commerce.web.dto.cart.PostCartDto;
import com.github.commerce.web.dto.cart.PutCartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/cart")
@RestController
public class CartController {
    private final CartService cartService;


    /**
     * 장바구니 전체조회
     * @param cursorId 기본값 0
     * @return
     */
    @GetMapping
    public ResponseEntity<List<CartDto>> getAllCart(
            @RequestParam(defaultValue = "0") Long cursorId
    ){
        Long userId = 1L;
        return ResponseEntity.ok(
                GetCartDto.Response.from(
                        cartService.getAllCart(userId, cursorId)
                )
        );
    }

    /**
     * 장바구니 상품추가
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<PostCartDto.Response> add(
            @RequestBody PostCartDto.Request request
            ){
        Long userId = 1L;
        return ResponseEntity.ok(
                PostCartDto.Response.from(cartService.addToCart(request, userId))
        );
    }

    /**
     * 장바구니 상품수정
     * @param request
     * @return
     */
    @PutMapping
    public ResponseEntity<CartDto> modify(
            @RequestBody PutCartDto.Request request
            ){
        Long userId = 1L;
        return ResponseEntity.ok(cartService.modifyCart(request, userId));
    }

    /**
     * 장바구니 전체삭제
     * @return
     */
    @DeleteMapping
    public ResponseEntity deleteAll(){
        Long userId = 1L;
        return null;
    }

    /**
     * 장바구니 개별삭제
     * @return
     */
    @DeleteMapping("/{cartId}")
    public ResponseEntity deleteOne(){
        Long userId = 1L;
        return null;
    }

}
