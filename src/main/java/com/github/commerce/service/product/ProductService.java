package com.github.commerce.service.product;

import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

public class ProductService {


//    @Transactional(readOnly=true)
//    public List<ProductResponseDto> getPopularTen(GetRequestDto getRequestDto) {
//        Integer animalCategory = convertCategory.convertAnimalCategory(getRequestDto.getAnimalCategory());
//        Integer productCategory = convertCategory.convertProductCategory(getRequestDto.getAnimalCategory(), getRequestDto.getProductCategory());
//
//        List<Product> products = productRepository.findTop10ByAnimalCategoryAndProductCategoryOrderByWishCountDesc(animalCategory, productCategory);
//        List<ProductDto> productList = products.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
//        List<ProductDto> checkedProducts = searchWishList.setIsLiked(productList);
//        return checkedProducts.stream().map(ProductResponseDto::fromEntity).sorted(Comparator.comparingInt(ProductResponseDto::getWishCount).reversed()).collect(Collectors.toList());
//    }
}
