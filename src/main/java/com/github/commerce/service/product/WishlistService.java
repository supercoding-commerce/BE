package com.github.commerce.service.product;

import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.entity.Wishlist;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.product.WishlistRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.product.util.ValidateProductMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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


}