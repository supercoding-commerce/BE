package com.github.commerce.repository.review;

import com.github.commerce.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT r.id, r.payment_histories_id, r.products_id, r.author, r.title, r.content, r.star_point, r.created_at " +
            "FROM reviews r " +
            "WHERE r.products_id = :productId AND r.is_deleted = :isDeleted AND r.id > :cursorId ",
            nativeQuery = true)
    List<Object[]> findReviewsByProductId(
            @Param("productId") Long productId,
            @Param("isDeleted") boolean b,
            @Param("cursorId") Integer cursorId,
            @Param("pageable") Pageable pageable
            );

    Review findByIdAndUsersIdAndIsDeleted(Long reviewId, Long userId, boolean b);

    boolean existsByOrdersIdAndProductsId(Long orderId, Long productId);
}
