package com.github.commerce.repository.product;

import com.github.commerce.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value="SELECT p FROM Product p " +
            "WHERE p.name LIKE :searchWord")
    List<Product> searchProduct(
            @Param("searchWord")String searchWord,
            @Param("pageable")Pageable pageable
    );
}
