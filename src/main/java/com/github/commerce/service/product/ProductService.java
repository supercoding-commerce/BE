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
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
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
    public List<GetProductDto> searchProducts(Integer pageNumber, String searchWord, String ageCategory, String genderCategory, String sortBy) {
        String inputAgeCategory = AgeCategoryEnum.switchCategory(ageCategory);
        String inputGenderCategory = GenderCategoryEnum.switchCategory(genderCategory);
        Pageable pageable = PageRequest.of(pageNumber - 1, 15); //한 페이지 15개
        String searchToken = "%"+searchWord+"%";

        if (Objects.equals(sortBy, "price")) {
            return productRepository.searchProductSortByPrice(searchToken, inputAgeCategory,inputGenderCategory, pageable);
           // return productList.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
        }else if(Objects.equals(sortBy, "createdAt")) {
            return productRepository.searchProductSortByCreatedAt(searchToken,inputAgeCategory,inputGenderCategory, pageable);
           // return productList.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
        }else {
            return productRepository.searchProductSortById(searchToken,inputAgeCategory,inputGenderCategory, pageable);
        }
    }

    //상품 등록
    @Transactional
    public String createProductItem(ProductRequest productRequest,  List<MultipartFile> imageFiles, Long profileId) {
        Seller seller = validateProductMethod.validateSeller(profileId);
        List<String> options = productRequest.getOptions();
        Gson gson = new Gson();
        String inputOptionsJson = gson.toJson(options);

        boolean imageExists = Optional.ofNullable(imageFiles).isPresent();
        System.out.println(imageExists);

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
                            .options(inputOptionsJson)
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
            throw new ProductException(ProductErrorCode.FAIL_TO_SAVE);
        }
    }

    @Transactional
    // 상품 삭제
    public void deleteProductByProductId(Long productId, Long profileId) {
        Product valiProduct = validProfileAndProduct(productId,profileId);
        productRepository.deleteById(valiProduct.getId());
    }

    @Transactional
    // 상품 수정
    public void updateProductById(Long productId, Long profileId, ProductRequest productRequest) {
        Product originProduct = validProfileAndProduct(productId,profileId);

        try {
            Product updateProduct = Product.from(originProduct,productRequest);
            productRepository.save(updateProduct);
        } catch (Exception e){
            throw new ProductException(ProductErrorCode.UNPROCESSABLE_ENTITY);
        }
    }

    private Product validProfileAndProduct(Long productId, Long profileId) {
//        Long validProfileId = Optional.ofNullable(profileId)
//                .orElseThrow(()-> new UserException(UserErrorCode.UER_NOT_FOUND));
        Seller seller = validateProductMethod.validateSeller(productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductException(ProductErrorCode.NOTFOUND_PRODUCT));
        if(!Objects.equals(product.getSeller().getId(), seller.getId())){
            throw new ProductException(ProductErrorCode.NOT_AUTHORIZED_SELLER);
        }
        return product;
    }


    @Transactional(readOnly = true)
    public ProductDto getOneProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ProductException(ProductErrorCode.NOTFOUND_PRODUCT));
        return ProductDto.fromEntityDetail(product);
    }

    @Transactional(readOnly = true)
    public List<GetProductDto> getProductsByCategory(Integer pageNumber, String productCategory, String ageCategory, String genderCategory, String sortBy) {
        String inputProductCategory = ProductCategoryEnum.switchCategory(productCategory);
        if(inputProductCategory == null) throw new ProductException(ProductErrorCode.INVALID_CATEGORY);

        Pageable pageable = PageRequest.of(pageNumber - 1, 15); //한 페이지 15개
        String inputAgeCategory = AgeCategoryEnum.switchCategory(ageCategory);
        String inputGenderCategory = GenderCategoryEnum.switchCategory(genderCategory);

        if (Objects.equals(sortBy, "createdAt")) {
            return productRepository.findByProductCategorySortByCreatedAt(inputProductCategory, inputAgeCategory, inputGenderCategory, pageable);
           // return products.stream().map(ProductDto::fromObjectResult).collect(Collectors.toList());
        }else if(Objects.equals(sortBy, "price")){
            return productRepository.findByProductCategorySortByPrice(inputProductCategory, inputAgeCategory, inputGenderCategory, pageable);
           // return products.stream().map(ProductDto::fromObjectResult).collect(Collectors.toList());
        } else {
            return productRepository.findByProductCategorySortById(inputProductCategory, inputAgeCategory, inputGenderCategory, pageable);
        }
    }

    @Transactional(readOnly = true)
    public List<GetProductDto> getProductList(Integer pageNumber, String ageCategory, String genderCategory, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 15); //한 페이지 15개
        String inputAgeCategory = AgeCategoryEnum.switchCategory(ageCategory);
        String inputGenderCategory = GenderCategoryEnum.switchCategory(genderCategory);

        if (Objects.equals(sortBy, "price")) {
           return productRepository.findAllSortByPrice(inputAgeCategory, inputGenderCategory, pageable);
            //return products.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
        } else if(Objects.equals(sortBy, "createdAt")) {
           return productRepository.findAllSortByCreatedAt(inputAgeCategory, inputGenderCategory, pageable);
            //return products.stream().map(ProductDto::fromEntity).collect(Collectors.toList());
        } else {
            return productRepository.findAllSortById(inputAgeCategory, inputGenderCategory, pageable);
        }
    }
}


