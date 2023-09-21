package com.github.commerce.repository.order;

import com.github.commerce.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
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

    @Query(
            "SELECT o, o.products.price, o.products.name, o.products.thumbnailUrl FROM Order o " +
                    "WHERE o.users.id = :userId " +
                    "AND o.orderState = 1 " +
                    //"SELECT c FROM Cart c " +
                    "ORDER BY o.createdAt DESC "
    )
    List<Order> findAllByUsersIdOrderByCreatedAtDesc(Long userId);

    @Query(
            "SELECT o, o.products.price, o.products.name, o.products.thumbnailUrl FROM Order o " +
                    "WHERE o.sellers.id = :sellerId " +
                    //"SELECT c FROM Cart c " +
                    "AND o.orderState in (2, 3, 4, 5) " +
                    "ORDER BY o.createdAt DESC "
    )
    List<Order> findPaidOrderBySellerIdSortByCreatedAtDesc(Long sellerId);

    @Query(
            "SELECT o, o.products.price, o.products.name, o.products.thumbnailUrl FROM Order o " +
                    "WHERE o.users.id = :userId " +
                    //"SELECT c FROM Cart c " +
                    "AND o.orderState in (2, 3, 4, 5) " +
                    "ORDER BY o.createdAt DESC "
    )
    List<Order> findPaidOrderByUserIdSortByCreatedAtDesc(Long userId);

    @Query(
            "SELECT o FROM Order o " +
                    "WHERE o.users.id = :userId " +
                    //"SELECT c FROM Cart c " +
                    "AND o.orderState in (2, 3, 4, 5) " +
                    "ORDER BY o.createdAt DESC "
    )
    List<Order> findAllByUsersIdForDetailPage(Long userId);

    @Query(
            "SELECT o FROM Order o " +
                    "WHERE o.id = :orderId " +
                    "AND o.orderState in (2, 3, 4, 5) "
    )
    Optional<Order> validatePaidOrderByOrderId(Long orderId);

    @Query("SELECT SUM(o.totalPrice) as totalPrice, o.users.id as userId FROM Order o " +
            "WHERE o.createdAt >= :oneMonthBefore and o.orderState > 1 GROUP BY o.users.id "
            )
    List<Map<String, Object>> getUserTotalPriceFromOneMonth(LocalDateTime oneMonthBefore);


    Optional<Order> findByCartsId(Long cartId);

    List<Order> findByUsersIdAndOrderTag(Long userId, String orderTag);

    List<Order> findByUsersIdAndProductsId(Long userId, Long productId);

    List<Order> findByUsersIdAndOrderTagAndOrderState(Long userId, String orderTag, int orderState);

    @Query(value = "SELECT * FROM orders ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Order findOrderOrderByIdDesc();
}


