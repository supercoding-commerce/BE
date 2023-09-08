package com.github.commerce.service.product;

import com.github.commerce.entity.Product;
import com.github.commerce.entity.Seller;
import com.github.commerce.entity.User;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.product.exception.ProductErrorCode;
import com.github.commerce.service.product.exception.ProductException;
import com.github.commerce.service.product.util.ValidateProductMethod;
import com.github.commerce.service.user.exception.UserErrorCode;
import com.github.commerce.service.user.exception.UserException;
import com.github.commerce.web.dto.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductImageUploadService productImageUploadService;
    private final ValidateProductMethod validateProductMethod;

    @Transactional
    public List<ProductDto> getProducts(Integer pageNumber, String searchWord, String sort) {

        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        String searchToken = "%"+searchWord+"%";
        String sortBy = sort;
        List<Product> productList = productRepository.searchProduct(searchToken, sortBy, pageable);
        return productList.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
    }

    //상품 등록
    @Transactional
    public String createProductItem(ProductRequest productRequest,  List<MultipartFile> imageFiles, Long profileId) {
        Seller seller = validateProductMethod.validateSeller(profileId);
        boolean imageExists = Optional.ofNullable(imageFiles).isPresent();

        if(imageExists && imageFiles.size() > 5) throw new ProductException(ProductErrorCode.TOO_MANY_FILES);

        try{
            Product product = productRepository.save(
                    Product.builder()
                            .name(productRequest.getName())
                            .seller(seller)
                            .price(productRequest.getPrice())
                            .content(productRequest.getContent())
                            .leftAmount(productRequest.getLeftAmount())
                            .createdAt(LocalDateTime.now())
                            .isDeleted(false)
                            .productCategory(ProductCategoryEnum.switchCategory(productRequest.getProductCategory()))
                            .ageCategory(AgeCategoryEnum.switchCategory(productRequest.getAgeCategory()))
                            .genderCategory(GenderCategoryEnum.switchCategory(productRequest.getGenderCategory()))
                            .build()
            );

            if(product.getId() != null && imageExists){
                List<String>urlList = productImageUploadService.uploadImageFileList(imageFiles);
                String joinedUrls = String.join(",", urlList);
                product.setThumbnailUrl(joinedUrls);
                return product.getName() + "상품이 등록되었습니다";

            }
            return product.getName() + "상품이 등록되었습니다";

        }catch (Exception e){
            throw new RuntimeException(e);
            //throw new ProductException(ProductErrorCode.FAIL_TO_SAVE);
        }
    }

    // 상품 삭제
    public void deleteProductByProductId(Long productId, Long profileId) {
        Product valiProduct = validProfileAndProduct(productId,profileId);
        productRepository.deleteById(valiProduct.getId());
    }

    // 상품 수정
    public void updateProductById(Long productId, Long profileId, ProductRequest productRequest) {
        Product originProduct = validProfileAndProduct(productId,profileId);

        try {
            Product updateProduct = Product.from(originProduct,productRequest);
            productRepository.save(updateProduct);
        } catch (Exception e){
            throw new ProductException(ProductErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Product validProfileAndProduct(Long productId, Long profileId) {
        Long validProfileId = Optional.ofNullable(profileId)
                .orElseThrow(()-> new UserException(UserErrorCode.UER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductException(ProductErrorCode.NOTFOUND_PRODUCT));
        if(!Objects.equals(product.getSeller().getId(),validProfileId)){
            throw new UserException(UserErrorCode.AUTHENTICATION_FAIL);
        }
        return product;
    }


    public ProductDto getOneProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ProductException(ProductErrorCode.NOTFOUND_PRODUCT));
        return ProductDto.fromEntity(product);
    }

    public List<ProductDto> getProductsByCategory(int productCategory, int ageCategory, int genderCategory, String sortBy) {
        String inputProductCategory = ProductCategoryEnum.switchCategory(productCategory);
        String inputAgeCategory = AgeCategoryEnum.switchCategory(ageCategory);
        String inputGenderCategory = GenderCategoryEnum.switchCategory(genderCategory);
        List<Product> products = productRepository.findByCategoryTab(inputProductCategory, inputAgeCategory, inputGenderCategory, sortBy );
        return products.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
    }
}


