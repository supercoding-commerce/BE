package com.github.commerce.repository.product;

import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    Wishlist findByUsers_IdAndProducts_Id(Long profileId, Long productId);

    Wishlist findByUsersAndProducts(User user, Product product);

    List<Wishlist> findByUsers_Id(Long id);

    List<Long> findProductIdsByUsers_Id(Long id);
}
