package com.github.commerce.repository.cart;

import com.github.commerce.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query(
            //"SELECT c FROM Cart c WHERE c.user.userId = :userId " +
            "SELECT c FROM Cart c " +
                    "WHERE c.cartId > :cursorId " +
                    "ORDER BY c.cartId ASC "
    )
    Page<Cart> findAllByCartId(Long cursorId, Pageable pageable);

    Cart findByCartId(Long cartId);

}
