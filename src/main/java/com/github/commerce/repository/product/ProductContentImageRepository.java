package com.github.commerce.repository.product;

import com.github.commerce.entity.ProductContentImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductContentImageRepository extends JpaRepository<ProductContentImage, Long> {
    List<ProductContentImage> findAllByProduct_Id(Long productId);
}
