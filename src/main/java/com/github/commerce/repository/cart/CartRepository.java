package com.github.commerce.repository.cart;

import com.github.commerce.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query(
            //"SELECT c FROM Cart c WHERE c.user.userId = :userId " +
            "SELECT c FROM Cart c " +
                    "WHERE c.id > :cursorId " +
                    "ORDER BY c.id ASC "
    )
    Page<Cart> findAllByCartId(Long cursorId, Pageable pageable);

    Optional<Cart> findById(Long cartId);

}
