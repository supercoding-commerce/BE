package com.github.commerce.repository.cart;

import com.github.commerce.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query(
            "SELECT c FROM Cart c " +
                    "WHERE c.users.id = :userId " +
            //"SELECT c FROM Cart c " +
                    "AND c.id > :cursorId " +
                    "ORDER BY c.id ASC "
    )
    Page<Cart> findAllByUserId(Long userId, Long cursorId, Pageable pageable);

    Optional<Cart> findById(Long cartId);

    Cart findByIdAndUsersId(Long id, Long userId);

    boolean existsByUsersIdAndProductsId(Long userId, Long productId);

    void deleteAllByUsersId(Long userId);

    List<Cart> findAllByUsersId(Long userId);

    @Query(
            "SELECT c, c.products.price, c.products.name, c.products.thumbnailUrl, c.products.leftAmount FROM Cart c " +
                    "WHERE c.users.id = :userId " +
                    "AND (c.cartState = 0 OR c.cartState = 2)" +
                    //"SELECT c FROM Cart c " +
                    "ORDER BY c.createdAt DESC "
    )
    List<Cart> findAllByUsersIdOrderByCreatedAtDesc(Long userId);
}
