package com.github.commerce.service.product;

import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.product.exception.ProductErrorCode;
import com.github.commerce.service.product.exception.ProductException;
import com.github.commerce.web.dto.product.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductImageUploadService productImageUploadService;

    //상품 등록
    @Transactional
    public void createProductItem(ProductRequest productRequest, MultipartFile thumbnailImage, List<MultipartFile> imageFiles, Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(null);
        boolean imageExists = Optional.ofNullable(imageFiles).isPresent();

        if(imageExists && imageFiles.size() > 5) throw new ProductException(ProductErrorCode.TOO_MANY_FILES);
        try{
            System.out.println("222222" + productRequest.getName());
            Product product = productRepository.save(
                    Product.builder()
                            .name(productRequest.getName())
                            .users(user)
                            .price(productRequest.getPrice())
                            .content(productRequest.getContent())
                            .leftAmount(productRequest.getLeftAmount())
                            .createdAt(LocalDateTime.now())
                            .isDeleted(false)
                            .productCategory("test")
                            .ageCategory("test")
                            .genderCategory("test")
                            .build()
            );
            System.out.println("33333");

            if(product.getId() != null){
                productImageUploadService.uploadThumbNailImage(thumbnailImage, product);
                if (imageExists) {
                   productImageUploadService.uploadImageFileList(imageFiles, product);
                }
            }
        }catch (Exception e){
            throw new ProductException(ProductErrorCode.FAIL_TO_SAVE);
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

