package com.github.commerce.repository.product;

import com.github.commerce.entity.ProductContentImage;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductContentImageRepository extends JpaRepository<ProductContentImage, Long> {
    List<ProductContentImage> findAllByProduct_Id(Long productId);

    @Modifying
    @Query("UPDATE ProductContentImage p SET p.imageUrl = ?1 WHERE p.imageUrl = ?2")
    void updateByImageUrl(String newImageUrl, String deleteImageUrl);

    void deleteByImageUrl(String imageUrl);

    List<ProductContentImage> findByProductId(Long id);
}
