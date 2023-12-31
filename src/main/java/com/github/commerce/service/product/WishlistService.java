package com.github.commerce.service.product;

import com.github.commerce.entity.Product;
import com.github.commerce.entity.Seller;
import com.github.commerce.entity.User;
import com.github.commerce.entity.Wishlist;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.product.WishlistRepository;
import com.github.commerce.repository.user.SellerRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.product.exception.ProductErrorCode;
import com.github.commerce.service.product.exception.ProductException;
import com.github.commerce.service.product.util.ValidateProductMethod;
import com.github.commerce.web.advice.custom.CustomException;
import com.github.commerce.web.advice.custom.ErrorCode;
import com.github.commerce.web.dto.product.GetProductDto;
import com.github.commerce.web.dto.product.ProductDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final ValidateProductMethod validateProductMethod;
    private final SellerRepository sellerRepository;

    public boolean addWishlist(User validateProfileId , Product validateProduct) {
        if (validateProfileId != null && validateProduct != null) {
            Wishlist existingWishlist = wishlistRepository.findByUsers_IdAndProducts_Id(validateProfileId.getId(),validateProduct.getId());
            if (existingWishlist == null) {
                Wishlist wishlist = Wishlist.builder()
                            .users(validateProfileId)
                            .products(validateProduct)
                            .createdAt(LocalDateTime.now())
                            .build();
                wishlistRepository.save(wishlist);
                return true;
                }
            }return false;
    }

    public boolean removeWishlist(User validateProfileId, Product validateProduct ) {
        if (validateProfileId != null && validateProduct != null) {
                Wishlist existingWishlist = wishlistRepository.findByUsers_IdAndProducts_Id(validateProfileId.getId(), validateProduct.getId());
                if (existingWishlist != null) {
                    wishlistRepository.delete(existingWishlist);
                return true;
                }
        }return false;
    }


    public List<Map<String, Object>> getWishlist(User validateProfileId) {
        List<Wishlist> wishlists = wishlistRepository.findProductsIdByUsers_Id(validateProfileId.getId());
        if(wishlists.isEmpty()) throw new CustomException(ErrorCode.NOT_FOUND_WISHLIST);

        return wishlists.stream()
                .sorted(Comparator.comparing(Wishlist::getCreatedAt).reversed()) // createdAt으로 정렬
                .map(wishlist -> {
                    Product product = wishlist.getProducts();
                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("productId", product.getId());
                    productMap.put("name", product.getName());
                    productMap.put("price", product.getPrice());
                    productMap.put("shopName", product.getSeller().getShopName());
                    productMap.put("thumbnailUrl", product.getThumbnailUrl());
                    return productMap;
        }).collect(Collectors.toList());
    }
}