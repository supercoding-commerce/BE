package com.github.commerce.repository.order;

import com.github.commerce.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(
            "SELECT o FROM Order o " +
                    "WHERE o.users.id = :userId " +
                    //"SELECT c FROM Cart c " +
                    "AND o.id > :cursorId " +
                    "ORDER BY o.id ASC "
    )
    Page<Order> findAllByUsersIdAndCursorId(Long userId, Long cursorId, PageRequest of);

    Optional<Order> findByIdAndUsersId(Long orderId, Long userId);

    List<Order> findAllByUsersIdOrderByCreatedAtDesc(Long userId);

    @Query(
            "SELECT o FROM Order o " +
                    "WHERE o.sellers.id = :sellerId " +
                    //"SELECT c FROM Cart c " +
                    "AND o.orderState in (2, 3, 4, 5) " +
                    "ORDER BY o.createdAt DESC "
    )
    List<Order> findPaidOrderBySellerIdSortByCreatedAtDesc(Long sellerId);

    @Query(
            "SELECT o FROM Order o " +
                    "WHERE o.users.id = :userId " +
                    //"SELECT c FROM Cart c " +
                    "AND o.orderState in (2, 3, 4, 5) " +
                    "ORDER BY o.createdAt DESC "
    )
    List<Order> findPaidOrderByUserIdSortByCreatedAtDesc(Long userId);

    Optional<Order> findByUsersId(Long userId);

    @Query(
            "SELECT o FROM Order o " +
                    "WHERE o.users.id = :userId AND o.products.id = :productId " +
                    "AND o.orderState in (2, 3, 4, 5) " +
                    "ORDER BY o.createdAt DESC "
    )
    Optional<Order> validatePaidOrderByUsersIdAndProductsId(Long userId, Long productId);
}
