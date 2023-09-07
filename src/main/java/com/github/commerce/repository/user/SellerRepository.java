package com.github.commerce.repository.user;

import com.github.commerce.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    boolean existsByShopName(String shopName);

    boolean existsByUsersId(Long userId);
}
