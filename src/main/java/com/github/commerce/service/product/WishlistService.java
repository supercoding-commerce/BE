package com.github.commerce.service.product;

import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.entity.Wishlist;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.product.WishlistRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@RequiredArgsConstructor
@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ValidateProductMethod validateProductMethod;


    public boolean addWishlist(User profileId, Product product) {
        if (profileId != null && product != null) {
            Wishlist existingWishlist = wishlistRepository.findByUsers_IdAndProducts_Id(profileId.getId(), product.getId());
            if (existingWishlist == null) {
                Wishlist wishlist = Wishlist.builder()
                            .users(profileId)
                            .products(product)
                            .build();
                wishlistRepository.save(wishlist);
                return true;
                }
            }return false;
    }

    public boolean removeWishlist(User profileId, Product product) {

        if (profileId != null && product != null) {
                Wishlist existingWishlist = wishlistRepository.findByUsers_IdAndProducts_Id(profileId.getId(), product.getId());
                if (existingWishlist != null) {
                    wishlistRepository.delete(existingWishlist);
                return true;
                }
        }return false;
    }


    public String getWishlist(User profileId) {
        List<Wishlist> wishlists = wishlistRepository.findProductsIdByUsers_Id(profileId.getId());

        if(wishlists.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_WISHLIST);
        }

        List<Long> productIds = wishlists.stream()
                .map(wishlist -> wishlist.getProducts().getId()) // 여기서 "products"는 엔터티 간의 관계를 나타내며, "getId()"는 제품 ID를 추출합니다.
                .collect(Collectors.toList());
        System.out.println(productIds);

        List<Product> products = productRepository.findProductsByProductIds(productIds);
        System.out.println(products);

        List<Map<String, Object>> productMaps = products.stream().map(product -> {
            Map<String, Object> productMap = new HashMap<>();
            productMap.put("id", product.getId());
            productMap.put("name", product.getName());
            productMap.put("price", product.getPrice());
            productMap.put("options",product.getOptions());
            productMap.put("thumbnailUrl",product.getThumbnailUrl());
            return productMap;
        }).collect(Collectors.toList());
        System.out.println(productMaps);

        Gson gson = new Gson();
        return gson.toJson(productMaps);
    }

}