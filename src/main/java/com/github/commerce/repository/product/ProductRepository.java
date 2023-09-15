package com.github.commerce.repository.product;

import com.github.commerce.entity.Product;
import com.github.commerce.web.dto.product.GetProductDto;
import com.github.commerce.web.dto.product.ProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value="SELECT NEW com.github.commerce.web.dto.product.GetProductDto(p.id, p.name, p.price,p.createdAt, p.productCategory, p.ageCategory, p.genderCategory, p.leftAmount, p.thumbnailUrl, p.seller.shopName) " +
            "FROM Product p " +
            "WHERE (:inputAgeCategory IS NULL OR p.ageCategory = :inputAgeCategory) " +
            "AND (:inputGenderCategory IS NULL OR p.genderCategory = :inputGenderCategory) " +
            "AND p.isDeleted = false " +
            "AND p.name LIKE :searchToken " +
            "ORDER BY p.price ASC"
    )
    List<GetProductDto> searchProductSortByPrice(
            @Param("searchToken")String searchToken,
            @Param("inputAgeCategory")String inputAgeCategory,
            @Param("inputGenderCategory")String inputGenderCategory,
            @Param("pageable")Pageable pageable
    );

    @Query(value="SELECT NEW com.github.commerce.web.dto.product.GetProductDto(p.id, p.name, p.price,p.createdAt, p.productCategory, p.ageCategory, p.genderCategory, p.leftAmount, p.thumbnailUrl, p.seller.shopName) " +
            "FROM Product p " +
            "WHERE (:inputAgeCategory IS NULL OR p.ageCategory = :inputAgeCategory) " +
            "AND (:inputGenderCategory IS NULL OR p.genderCategory = :inputGenderCategory) " +
            "AND p.isDeleted = false " +
            "AND p.name LIKE :searchToken " +
            "ORDER BY p.createdAt DESC"
    )
    List<GetProductDto> searchProductSortByCreatedAt(
            @Param("searchToken")String searchToken,
            @Param("inputAgeCategory")String inputAgeCategory,
            @Param("inputGenderCategory")String inputGenderCategory,
            @Param("pageable")Pageable pageable
    );

    @Query(value="SELECT NEW com.github.commerce.web.dto.product.GetProductDto(p.id, p.name, p.price,p.createdAt, p.productCategory, p.ageCategory, p.genderCategory, p.leftAmount, p.thumbnailUrl, p.seller.shopName) " +
            "FROM Product p " +
            "WHERE (:inputAgeCategory IS NULL OR p.ageCategory = :inputAgeCategory) " +
            "AND (:inputGenderCategory IS NULL OR p.genderCategory = :inputGenderCategory) " +
            "AND p.isDeleted = false " +
            "AND p.name LIKE :searchToken " +
            "ORDER BY p.id ASC"
    )
    List<GetProductDto> searchProductSortById(
            @Param("searchToken")String searchToken,
            @Param("inputAgeCategory")String inputAgeCategory,
            @Param("inputGenderCategory")String inputGenderCategory,
            @Param("pageable")Pageable pageable
    );


    @Query(value="SELECT NEW com.github.commerce.web.dto.product.GetProductDto(p.id, p.name, p.price,p.createdAt, p.productCategory, p.ageCategory, p.genderCategory, p.leftAmount, p.thumbnailUrl, p.seller.shopName) " +
            "FROM Product p " +
            "WHERE (:inputAgeCategory IS NULL OR p.ageCategory = :inputAgeCategory) " +
            "AND (:inputGenderCategory IS NULL OR p.genderCategory = :inputGenderCategory) " +
            "AND p.isDeleted = false " +
            "AND p.name LIKE :searchToken "
    )
    List<GetProductDto> searchProductWithoutSort(
            @Param("searchToken")String searchToken,
            @Param("inputAgeCategory")String inputAgeCategory,
            @Param("inputGenderCategory")String inputGenderCategory,
            @Param("pageable")Pageable pageable
    );

    @Query(value = "SELECT NEW com.github.commerce.web.dto.product.GetProductDto(p.id, p.name, p.price,p.createdAt, p.productCategory, p.ageCategory, p.genderCategory, p.leftAmount, p.thumbnailUrl, p.seller.shopName) " +
            "FROM Product p " +
            "WHERE p.productCategory = :inputProductCategory " +
            "AND (:inputAgeCategory IS NULL OR p.ageCategory = :inputAgeCategory) " +
            "AND (:inputGenderCategory IS NULL OR p.genderCategory = :inputGenderCategory) " +
            "AND p.isDeleted = false " +
            "ORDER BY p.createdAt DESC"
    )
    List<GetProductDto> findByProductCategorySortByCreatedAt(
            @Param("inputProductCategory")String inputProductCategory,
            @Param("inputAgeCategory")String inputAgeCategory,
            @Param("inputGenderCategory")String inputGenderCategory,
            @Param("pageable")Pageable pageable
    );

    @Query(value = "SELECT NEW com.github.commerce.web.dto.product.GetProductDto(p.id, p.name, p.price,p.createdAt, p.productCategory, p.ageCategory, p.genderCategory, p.leftAmount, p.thumbnailUrl, p.seller.shopName) " +
            "FROM Product p " +
            "WHERE p.productCategory = :inputProductCategory " +
            "AND (:inputAgeCategory IS NULL OR p.ageCategory = :inputAgeCategory) " +
            "AND (:inputGenderCategory IS NULL OR p.genderCategory = :inputGenderCategory) " +
            "AND p.isDeleted = false " +
            "ORDER BY p.price ASC"
    )
    List<GetProductDto> findByProductCategorySortByPrice(
            @Param("inputProductCategory")String inputProductCategory,
            @Param("inputAgeCategory")String inputAgeCategory,
            @Param("inputGenderCategory")String inputGenderCategory,
            @Param("pageable")Pageable pageable
    );

    @Query(value = "SELECT NEW com.github.commerce.web.dto.product.GetProductDto(p.id, p.name, p.price,p.createdAt, p.productCategory, p.ageCategory, p.genderCategory, p.leftAmount, p.thumbnailUrl, p.seller.shopName) " +
            "FROM Product p " +
            "WHERE p.productCategory = :inputProductCategory " +
            "AND (:inputAgeCategory IS NULL OR p.ageCategory = :inputAgeCategory) " +
            "AND (:inputGenderCategory IS NULL OR p.genderCategory = :inputGenderCategory)" +
            "AND p.isDeleted = false " +
            "ORDER BY p.id ASC"
    )
    List<GetProductDto> findByProductCategorySortById(
            @Param("inputProductCategory")String inputProductCategory,
            @Param("inputAgeCategory")String inputAgeCategory,
            @Param("inputGenderCategory")String inputGenderCategory,
            @Param("pageable")Pageable pageable
    );


    @Query(value = "SELECT NEW com.github.commerce.web.dto.product.GetProductDto(p.id, p.name, p.price,p.createdAt, p.productCategory, p.ageCategory, p.genderCategory, p.leftAmount, p.thumbnailUrl, p.seller.shopName) " +
            "FROM Product p " +
            "WHERE (:inputAgeCategory IS NULL OR p.ageCategory = :inputAgeCategory) " +
            "AND (:inputGenderCategory IS NULL OR p.genderCategory = :inputGenderCategory) " +
            "AND p.isDeleted = false " +
            "ORDER BY p.price ASC"
    )
    List<GetProductDto> findAllSortByPrice(
            @Param("inputAgeCategory")String inputAgeCategory,
            @Param("inputGenderCategory")String inputGenderCategory,
            @Param("pageable")Pageable pageable
    );

    @Query(value = "SELECT NEW com.github.commerce.web.dto.product.GetProductDto(p.id, p.name, p.price,p.createdAt, p.productCategory, p.ageCategory, p.genderCategory, p.leftAmount, p.thumbnailUrl, p.seller.shopName) " +
            "FROM Product p " +
            "WHERE (:inputAgeCategory IS NULL OR p.ageCategory = :inputAgeCategory) " +
            "AND (:inputGenderCategory IS NULL OR p.genderCategory = :inputGenderCategory) " +
            "AND p.isDeleted = false " +
            "ORDER BY p.createdAt DESC"
    )
    List<GetProductDto> findAllSortByCreatedAt(
            @Param("inputAgeCategory")String inputAgeCategory,
            @Param("inputGenderCategory")String inputGenderCategory,
            @Param("pageable")Pageable pageable
    );

    @Query(value = "SELECT NEW com.github.commerce.web.dto.product.GetProductDto(p.id, p.name, p.price,p.createdAt, p.productCategory, p.ageCategory, p.genderCategory, p.leftAmount, p.thumbnailUrl, p.seller.shopName) " +
            "FROM Product p " +
            "WHERE (:inputAgeCategory IS NULL OR p.ageCategory = :inputAgeCategory) " +
            "AND (:inputGenderCategory IS NULL OR p.genderCategory = :inputGenderCategory) " +
            "AND p.isDeleted = false " +
            "ORDER BY p.id ASC"
    )
    List<GetProductDto> findAllSortById(
            @Param("inputAgeCategory")String inputAgeCategory,
            @Param("inputGenderCategory")String inputGenderCategory,
            @Param("pageable")Pageable pageable
    );
    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findProductsByProductIds(@Param("productIds") List<Long> productIds);
}
