package com.github.commerce.repository.product;
import com.github.commerce.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    Wishlist findByUsers_IdAndProducts_Id(Long profileId, Long productId);
    List<Wishlist> findProductsIdByUsers_Id(Long id);

}

