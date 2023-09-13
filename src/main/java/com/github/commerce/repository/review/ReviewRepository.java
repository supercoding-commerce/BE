package com.github.commerce.repository.review;

import com.github.commerce.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT r " +
            "FROM Review r " +
            "WHERE r.products.id = :productId AND r.isDeleted = :isDeleted AND r.id > :cursorId ")
    List<Review> findReviewsByProductId(
            @Param("productId") Long productId,
            @Param("isDeleted") boolean isDeleted,
            @Param("cursorId") Long cursorId
            );

    Review findByIdAndUsersIdAndIsDeleted(Long reviewId, Long userId, boolean b);

    boolean existsByOrdersIdAndProductsId(Long orderId, Long productId);
}
