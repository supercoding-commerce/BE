package com.github.commerce.repository.product;

import com.github.commerce.web.dto.product.GetProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public List<GetProductDto> findByProductCategorySortByCreatedAt(
            String inputProductCategory,
            String inputAgeCategory,
            String inputGenderCategory,
            Pageable pageable) {

        String sql = "SELECT p.id, p.name, p.price, p.created_at, " +
                "p.product_category, p.age_category, p.gender_category, " +
                "p.left_amount, p.thumbnail_url, s.shop_name " +
                "FROM commerce.products p LEFT JOIN commerce.sellers s ON p.sellers_id = s.id " +
                "WHERE p.product_category = :inputProductCategory " +
                "AND (:inputAgeCategory IS NULL OR p.age_category = :inputAgeCategory) " +
                "AND (:inputGenderCategory IS NULL OR p.gender_category = :inputGenderCategory) " +
                "AND p.is_deleted = false " +
                "ORDER BY p.created_at DESC";

        Query query = entityManager.createNativeQuery(sql, "GetProductDtoMapping");
        query.setParameter("inputProductCategory", inputProductCategory);
        query.setParameter("inputAgeCategory", inputAgeCategory);
        query.setParameter("inputGenderCategory", inputGenderCategory);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        //noinspection unchecked
        return query.getResultList();
    }

    public List<GetProductDto> findByProductCategorySortByPrice(
            String inputProductCategory,
            String inputAgeCategory,
            String inputGenderCategory,
            Pageable pageable) {

        String sql = "SELECT p.id, p.name, p.price, p.created_at, " +
                "p.product_category, p.age_category, p.gender_category, " +
                "p.left_amount, p.thumbnail_url, s.shop_name " +
                "FROM commerce.products p LEFT JOIN commerce.sellers s ON p.sellers_id = s.id " +
                "WHERE p.product_category = :inputProductCategory " +
                "AND (:inputAgeCategory IS NULL OR p.age_category = :inputAgeCategory) " +
                "AND (:inputGenderCategory IS NULL OR p.gender_category = :inputGenderCategory) " +
                "AND p.is_deleted = false " +
                "ORDER BY p.price ASC";

        Query query = entityManager.createNativeQuery(sql, "GetProductDtoMapping");
        query.setParameter("inputProductCategory", inputProductCategory);
        query.setParameter("inputAgeCategory", inputAgeCategory);
        query.setParameter("inputGenderCategory", inputGenderCategory);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        //noinspection unchecked
        return query.getResultList();
    }

    public List<GetProductDto> findByProductCategorySortById(
            String inputProductCategory,
            String inputAgeCategory,
            String inputGenderCategory,
            Pageable pageable) {

        String sql = "SELECT p.id, p.name, p.price, p.created_at, " +
                "p.product_category, p.age_category, p.gender_category, " +
                "p.left_amount, p.thumbnail_url, s.shop_name " +
                "FROM commerce.products p LEFT JOIN commerce.sellers s ON p.sellers_id = s.id " +
                "WHERE p.product_category = :inputProductCategory " +
                "AND (:inputAgeCategory IS NULL OR p.age_category = :inputAgeCategory) " +
                "AND (:inputGenderCategory IS NULL OR p.gender_category = :inputGenderCategory) " +
                "AND p.is_deleted = false " +
                "ORDER BY p.id ASC";

        Query query = entityManager.createNativeQuery(sql, "GetProductDtoMapping");
        query.setParameter("inputProductCategory", inputProductCategory);
        query.setParameter("inputAgeCategory", inputAgeCategory);
        query.setParameter("inputGenderCategory", inputGenderCategory);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        //noinspection unchecked
        return query.getResultList();
    }

    public List<GetProductDto> findAllSortByCreatedAt(
            String inputAgeCategory,
            String inputGenderCategory,
            Pageable pageable) {
        String sql = "SELECT p.id, p.name, p.price, p.created_at, " +
                "p.product_category, p.age_category, p.gender_category, " +
                "p.left_amount, p.thumbnail_url, s.shop_name " +
                "FROM commerce.products p LEFT JOIN commerce.sellers s ON p.sellers_id = s.id " +
                "WHERE (:inputAgeCategory IS NULL OR p.age_category = :inputAgeCategory) " +
                "AND (:inputGenderCategory IS NULL OR p.gender_category = :inputGenderCategory) " +
                "AND p.is_deleted = false " +
                "ORDER BY p.created_at DESC";

        Query query = entityManager.createNativeQuery(sql, "GetProductDtoMapping");
        query.setParameter("inputAgeCategory", inputAgeCategory);
        query.setParameter("inputGenderCategory", inputGenderCategory);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        //noinspection unchecked
        return query.getResultList();
    }

    public List<GetProductDto> findAllSortByPrice(
            String inputAgeCategory,
            String inputGenderCategory,
            Pageable pageable) {
        String sql = "SELECT p.id, p.name, p.price, p.created_at, " +
                "p.product_category, p.age_category, p.gender_category, " +
                "p.left_amount, p.thumbnail_url, s.shop_name " +
                "FROM commerce.products p LEFT JOIN commerce.sellers s ON p.sellers_id = s.id " +
                "WHERE (:inputAgeCategory IS NULL OR p.age_category = :inputAgeCategory) " +
                "AND (:inputGenderCategory IS NULL OR p.gender_category = :inputGenderCategory) " +
                "AND p.is_deleted = false " +
                "ORDER BY p.price ASC";

        Query query = entityManager.createNativeQuery(sql, "GetProductDtoMapping");
        query.setParameter("inputAgeCategory", inputAgeCategory);
        query.setParameter("inputGenderCategory", inputGenderCategory);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        //noinspection unchecked
        return query.getResultList();
    }

    public List<GetProductDto> findAllSortById(
            String inputAgeCategory,
            String inputGenderCategory,
            Pageable pageable) {
        String sql = "SELECT p.id, p.name, p.price, p.created_at, " +
                "p.product_category, p.age_category, p.gender_category, " +
                "p.left_amount, p.thumbnail_url, s.shop_name " +
                "FROM commerce.products p LEFT JOIN commerce.sellers s ON p.sellers_id = s.id " +
                "WHERE (:inputAgeCategory IS NULL OR p.age_category = :inputAgeCategory) " +
                "AND (:inputGenderCategory IS NULL OR p.gender_category = :inputGenderCategory) " +
                "AND p.is_deleted = false " +
                "ORDER BY p.id ASC";

        Query query = entityManager.createNativeQuery(sql, "GetProductDtoMapping");
        query.setParameter("inputAgeCategory", inputAgeCategory);
        query.setParameter("inputGenderCategory", inputGenderCategory);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        //noinspection unchecked
        return query.getResultList();
    }

    public List<GetProductDto> searchProductSortByPrice(
            String searchToken,
            String inputAgeCategory,
            String inputGenderCategory,
            Pageable pageable) {
            String sql = "SELECT p.id, p.name, p.price, p.created_at, " +
                    "p.product_category, p.age_category, p.gender_category, " +
                    "p.left_amount, p.thumbnail_url, s.shop_name " +
                    "FROM commerce.products p LEFT JOIN commerce.sellers s ON p.sellers_id = s.id " +
                    "WHERE (:inputAgeCategory IS NULL OR p.age_category = :inputAgeCategory) " +
                    "AND (:inputGenderCategory IS NULL OR p.gender_category = :inputGenderCategory) " +
                    "AND p.is_deleted = false " +
                    "AND p.name LIKE :searchToken " +
                    "ORDER BY p.price ASC";

            Query query = entityManager.createNativeQuery(sql, "GetProductDtoMapping");
            query.setParameter("searchToken", searchToken);
            query.setParameter("inputAgeCategory", inputAgeCategory);
            query.setParameter("inputGenderCategory", inputGenderCategory);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            //noinspection unchecked
            return query.getResultList();
    }


    public List<GetProductDto> searchProductSortByCreatedAt(String searchToken, String inputAgeCategory, String inputGenderCategory, Pageable pageable)
    {
            String sql = "SELECT p.id, p.name, p.price, p.created_at, " +
                    "p.product_category, p.age_category, p.gender_category, " +
                    "p.left_amount, p.thumbnail_url, s.shop_name " +
                    "FROM commerce.products p LEFT JOIN commerce.sellers s ON p.sellers_id = s.id " +
                    "WHERE (:inputAgeCategory IS NULL OR p.age_category = :inputAgeCategory) " +
                    "AND (:inputGenderCategory IS NULL OR p.gender_category = :inputGenderCategory) " +
                    "AND p.is_deleted = false " +
                    "AND p.name LIKE :searchToken " +
                    "ORDER BY p.created_at DESC";

            Query query = entityManager.createNativeQuery(sql, "GetProductDtoMapping");
            query.setParameter("searchToken", searchToken);
            query.setParameter("inputAgeCategory", inputAgeCategory);
            query.setParameter("inputGenderCategory", inputGenderCategory);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            //noinspection unchecked
            return query.getResultList();

    }

    public List<GetProductDto> searchProductSortById(String searchToken, String inputAgeCategory, String inputGenderCategory, Pageable pageable) {
        String sql = "SELECT p.id, p.name, p.price, p.created_at, " +
                "p.product_category, p.age_category, p.gender_category, " +
                "p.left_amount, p.thumbnail_url, s.shop_name " +
                "FROM commerce.products p LEFT JOIN commerce.sellers s ON p.sellers_id = s.id " +
                "WHERE (:inputAgeCategory IS NULL OR p.age_category = :inputAgeCategory) " +
                "AND (:inputGenderCategory IS NULL OR p.gender_category = :inputGenderCategory) " +
                "AND p.is_deleted = false " +
                "AND p.name LIKE :searchToken " +
                "ORDER BY p.id ASC";

        Query query = entityManager.createNativeQuery(sql, "GetProductDtoMapping");
        query.setParameter("searchToken", searchToken);
        query.setParameter("inputAgeCategory", inputAgeCategory);
        query.setParameter("inputGenderCategory", inputGenderCategory);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        //noinspection unchecked
        return query.getResultList();

    }
}
