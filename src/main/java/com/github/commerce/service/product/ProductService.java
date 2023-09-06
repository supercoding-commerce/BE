package com.github.commerce.service.product;

import com.github.commerce.entity.Product;
import com.github.commerce.entity.mongocollection.ProductOption;
import com.github.commerce.repository.product.ProductOptionRepository;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.service.product.exception.ProductErrorCode;
import com.github.commerce.service.product.exception.ProductException;
import com.github.commerce.web.dto.product.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;
    private final ProductImageUploadService productImageUploadService;

    public ProductOption getMongo(int productId) {
       return productOptionRepository.findProductOptionByProductId(productId);
    }

    //상품 등록
    @Transactional
    public void createProductItem(ProductRequest productRequest, MultipartFile thumbnailImage, List<MultipartFile> imageFiles) {
       // TODO -> 판매자 정보 추가해야함 어떤 판매자인지 알아야하니깐
        boolean imageExists = Optional.ofNullable(imageFiles).isPresent();
        try{
            Product product = Product.from(productRequest);
            productRepository.save(product);
            if(product.getId() != null){
                CompletableFuture<Void> thumbFuture = productImageUploadService.uploadThumbNailImage(thumbnailImage, product);
                thumbFuture.join();
                if (imageExists) {
                    CompletableFuture<Void> imageListFuture = productImageUploadService.uploadImageFileList(imageFiles, product);
                    imageListFuture.join();
                }
            }
        }catch (Exception e){
            throw new ProductException(ProductErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}


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

