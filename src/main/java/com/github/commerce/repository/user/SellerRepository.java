package com.github.commerce.repository.user;

import com.github.commerce.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    boolean existsByShopNameAndUsersIsDeleteFalse(String shopName);

    boolean existsByUsersId(Long userId);

    Optional<Seller> findByUsersId(Long userId);
}
