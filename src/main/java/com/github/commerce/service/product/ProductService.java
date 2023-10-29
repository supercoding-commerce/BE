package com.github.commerce.service.product;

import com.github.commerce.entity.*;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.product.ProductContentImageRepository;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.product.ProductRepositoryCustom;
import com.github.commerce.repository.review.ReviewRepository;
import com.github.commerce.service.product.exception.ProductErrorCode;
import com.github.commerce.service.product.exception.ProductException;
import com.github.commerce.service.product.util.ValidateProductMethod;
import com.github.commerce.web.advice.custom.CustomException;
import com.github.commerce.web.dto.order.DetailPageOrderDto;
import com.github.commerce.web.dto.product.*;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepositoryCustom productRepositoryCustom;
    private final ProductRepository productRepository;
    private final ProductImageUploadService productImageUploadService;
    private final ValidateProductMethod validateProductMethod;
    private final OrderRepository orderRepository;
    private final ProductContentImageRepository productContentImageRepository;
    private final ReviewRepository reviewRepository;
    private final AwsS3Service awsS3Service;

    @Transactional(readOnly = true)
    public List<GetProductDto> searchProducts(Integer pageNumber, String searchWord, String ageCategory, String genderCategory, String sortBy) {
        String inputAgeCategory = AgeCategoryEnum.switchCategory(ageCategory);
        String inputGenderCategory = GenderCategoryEnum.switchCategory(genderCategory);
        Pageable pageable = PageRequest.of(pageNumber - 1, 15); //한 페이지 15개
        String searchToken = "%"+searchWord+"%";

        if (Objects.equals(sortBy, "price")) {
            return productRepositoryCustom.searchProductSortByPrice(searchToken, inputAgeCategory,inputGenderCategory, pageable);
        }else if(Objects.equals(sortBy, "createdAt")) {
            return productRepositoryCustom.searchProductSortByCreatedAt(searchToken,inputAgeCategory,inputGenderCategory, pageable);
        }else {
            return productRepositoryCustom.searchProductSortById(searchToken,inputAgeCategory,inputGenderCategory, pageable);
        }
    }

    //상품 등록
    @Transactional
    public ProductDto createProductItem(String productRequest,  List<MultipartFile> imageFiles, Long profileId) {
        Seller seller = validateProductMethod.validateSeller(profileId);
        boolean isSeller = validateProductMethod.isThisProductSeller(seller.getId(), profileId);

        Gson gson = new Gson();
        ProductRequest convertedRequest = gson.fromJson(productRequest, ProductRequest.class);
        List<String> options = convertedRequest.getOptions();
        String inputOptionsJson = gson.toJson(options);

        List<String> imageUrls;

        boolean imageExists = Optional.ofNullable(imageFiles).isPresent();
        if(imageExists && imageFiles.size() > 5) throw new ProductException(ProductErrorCode.TOO_MANY_FILES);

        try{
            Product product = productRepository.save(
                    Product.builder()
                            .name(convertedRequest.getName())
                            .seller(seller)
                            .price(convertedRequest.getPrice())
                            .content(convertedRequest.getContent())
                            .leftAmount(convertedRequest.getLeftAmount())
                            .createdAt(LocalDateTime.now())
                            .isDeleted(false)
                            .productCategory(ProductCategoryEnum.switchCategory(convertedRequest.getProductCategory()))
                            .ageCategory(AgeCategoryEnum.switchCategory(convertedRequest.getAgeCategory()))
                            .genderCategory(GenderCategoryEnum.switchCategory(convertedRequest.getGenderCategory()))
                            .options(inputOptionsJson)
                            .build()
            );

            if(product.getId() != null && imageExists){
                validateProductMethod.validateImage(imageFiles);
                List<String> urlList = productImageUploadService.uploadImageFileList(imageFiles);
                imageUrls = urlList;
                for (String url : urlList) {
                    productContentImageRepository.save(ProductContentImage.from(product, url));
                }


                    String firstUrl = urlList.get(0);
                    productContentImageRepository.deleteByImageUrl(firstUrl);
                    product.setThumbnailUrl(firstUrl);
                    imageUrls.remove(firstUrl);
                    productRepository.save(product);
                    return ProductDto.fromEntity(product,isSeller, imageUrls);

            }
            return ProductDto.fromEntity(product,isSeller, null);

        }catch (Exception e){
            throw new ProductException(ProductErrorCode.FAIL_TO_SAVE);
        }
    }
    @Transactional
    // 상품 수정
    public ProductDto updateProductById(Long productId, Long profileId, String productRequest,MultipartFile thumbnailFile, List<MultipartFile> imageFiles) {
        Seller validateSeller = validateProductMethod.validateSeller(profileId);
        Product validateProduct = validateProductMethod.validateProduct(productId);
        Product originProduct = productRepository.findBySellerIdAndId(validateSeller.getId(),validateProduct.getId());
        boolean isSeller = validateProductMethod.isThisProductSeller(validateSeller.getId(), profileId);
        // 판매자가 등록한 상품이 아닐 경우 예외처리
        if(originProduct == null) throw new ProductException(ProductErrorCode.NOT_AUTHORIZED_SELLER);


        Gson gson = new Gson();
        UpdateProductRequest convertedRequest = gson.fromJson(productRequest, UpdateProductRequest.class);
        List<String> options = convertedRequest.getOptions();
        List<String> targetImageUrls = convertedRequest.getDeleteImageUrls();
        String targetThumbnailUrl = convertedRequest.getDeleteThumbnailUrl();
        String inputOptionsJson = gson.toJson(options);
        List<String> newImageFiles = new ArrayList<>();

        // 기존에 있는 이미지들을 가져와 newImageFiles에 넣기
        List<ProductContentImage> existingImages = productContentImageRepository.findByProductId(originProduct.getId());
        for (ProductContentImage existingImage : existingImages) {
            newImageFiles.add(existingImage.getImageUrl());
        }

        // 기존에 있는 이미지 수 - 삭제할 이미지 수 + 새로 추가될 이미지 수가 5를 넘으면 예외처리
        int imageFilesSize = (imageFiles != null) ? imageFiles.size() : 0;
        if((newImageFiles.size() - targetImageUrls.size() + imageFilesSize) > 5) throw new ProductException(ProductErrorCode.TOO_MANY_FILES);

        boolean imageExists = Optional.ofNullable(imageFiles).isPresent();                                        // 새로운 이미지 파일들 추가
        boolean thumbnailExists = Optional.ofNullable(thumbnailFile).isPresent();                                 // 새로운 썸네일 이미지 추가
        boolean deleteUrlsExists= Optional.ofNullable(targetImageUrls).isPresent() && targetImageUrls.size() > 0; // 기존에 있는 이미지 파일들 수정요청
        boolean deleteThumbnailUrlExists= Optional.ofNullable(targetThumbnailUrl).isPresent();                   // 기존에 있는 썸네일 이미지 파일 수정요청
        if(imageExists && imageFiles.size() > 5) throw new ProductException(ProductErrorCode.TOO_MANY_FILES);

        try {
            originProduct.setName(convertedRequest.getName());
            originProduct.setContent(convertedRequest.getContent());
            originProduct.setPrice(convertedRequest.getPrice());
            originProduct.setLeftAmount(convertedRequest.getLeftAmount());
            originProduct.setUpdatedAt(LocalDateTime.now());
            originProduct.setProductCategory(ProductCategoryEnum.switchCategory(convertedRequest.getProductCategory()));
            originProduct.setGenderCategory(GenderCategoryEnum.switchCategory(convertedRequest.getGenderCategory()));
            originProduct.setAgeCategory(AgeCategoryEnum.switchCategory(convertedRequest.getAgeCategory()));
            originProduct.setOptions(inputOptionsJson);
            originProduct.setIsDeleted(false);


            // 썸네일 이미지 삭제하고 새로운 이미지 추가, 또는 기존 썸네일 이미지 그대로 사용 (왜냐하면 상품이미지 한개는 필수이므로)
            if (deleteThumbnailUrlExists) {
                if (thumbnailExists) {
                    awsS3Service.removeFile(targetThumbnailUrl);
                    String newThumbUrl = productImageUploadService.uploadImageFile(thumbnailFile);
                    originProduct.setThumbnailUrl(newThumbUrl);
                } else {originProduct.setThumbnailUrl(originProduct.getThumbnailUrl());}

            }
            // 기존 상품 삭제 요청이 있는 경우
            if (deleteUrlsExists) {
                // 기존 이미지 삭제, 새로운 이미지 추가
                if (imageExists) {
                    validateProductMethod.validateImage(imageFiles);
                    for (String targetImageUrl : targetImageUrls) {
                        if (targetImageUrl.equals(targetThumbnailUrl)) {
                            productContentImageRepository.deleteByImageUrl(targetImageUrl);
                        } else {
                            awsS3Service.removeFile(targetImageUrl);
                            newImageFiles.remove(targetImageUrl);
                            productContentImageRepository.deleteByImageUrl(targetImageUrl);
                        }
                    }
                    for (MultipartFile imageFile : imageFiles) {
                        String savedImageFiles = productImageUploadService.uploadImageFile(imageFile);
                        productContentImageRepository.save(ProductContentImage.from(originProduct, savedImageFiles));
                        newImageFiles.add(savedImageFiles);
                    }

                } else {
                    targetImageUrls.forEach((imageUrl) -> {
                        if (imageUrl.equals(targetThumbnailUrl)) {
                            // imageUrl과 targetThumbnailUrl이 같으면 실행할 작업
                            productContentImageRepository.deleteByImageUrl(imageUrl);
                        } else {
                            // imageUrl와 targetThumbnailUrl이 다르면 실행할 작업
                            awsS3Service.removeFile(imageUrl);
                            productContentImageRepository.deleteByImageUrl(imageUrl);
                        }
                    });
                }
            } else {
                // 기존 이미지 수정 없고 이미지 추가 될 경우
                if(thumbnailExists) throw new ProductException(ProductErrorCode.NOT_FOUND_SAVEDTHUMBNAILFILE);
                if (imageFiles != null) {
                    for (MultipartFile imageFile : imageFiles) {
                        String savedImageFiles = productImageUploadService.uploadImageFile(imageFile);
                        productContentImageRepository.save(ProductContentImage.from(originProduct, savedImageFiles));
                        newImageFiles.add(savedImageFiles);
                    }
                }
            }
        } catch (ProductException e){
            throw new ProductException(ProductErrorCode.TOO_MANY_FILES);
        }
    return ProductDto.fromUpdateEntity(originProduct,isSeller,newImageFiles);
    }

    @Transactional
    // 상품 삭제
    public void deleteProductByProductId(Long productId, Long profileId) {
        // 로그인한 user가 seller인지 확인
        Seller validateSeller = validateProductMethod.validateSeller(profileId);
        // 조회하려는 상품 존재하는지 확인
        Product validateProduct = validateProductMethod.validateProduct(productId);
        // 로그인한 판매자가 등록한 상품이 맞으면 삭제,아니면 예외처리
        Product existingProduct = productRepository.findBySellerIdAndId(validateSeller.getId(), validateProduct.getId());
        if (existingProduct != null) {
            productRepository.delete(existingProduct);
        } else {
            throw new ProductException(ProductErrorCode.NOT_AUTHORIZED_SELLER);
        }
    }

    @Transactional(readOnly = true)
    public ProductDto getOneProduct(Long productId, Long userId, String userName) {
        List<String> imageUrlList = new ArrayList<>();
        Product product = productRepository.findById(productId).orElseThrow(()-> new ProductException(ProductErrorCode.NOTFOUND_PRODUCT));

        //이미지 배열 불러오기
        List<ProductContentImage> productImages = productContentImageRepository.findAllByProduct_Id(productId);
        productImages.forEach(p -> {
            imageUrlList.add(p.getImageUrl());
        });

        //채팅관련기능 : 로그인한 유저의 seller flag - true or false
        boolean isSeller = validateProductMethod.isThisProductSeller(product.getSeller().getId(), userId);

        //리뷰관련기능 : 로그인한 유저의 결제완료 주문내역
        List<Order> orderList = orderRepository.findAllByUsersIdForDetailPage(userId);
        List<DetailPageOrderDto> orderDtoList = orderList.stream().map(DetailPageOrderDto::fromEntity).collect(Collectors.toList());

        //리뷰관련기능 : 별점 평균
        Double averageStar = null;
         List<Review> reviewList = reviewRepository.findReviewsByProductId(productId, false, 0L);
        Optional<Short> optionalSum = reviewList.stream()
                .map(Review::getStarPoint)
                .reduce((s1, s2) -> (short) (s1 + s2));  // 명시적으로 Short 타입에 대한 덧셈을 수행

        if (optionalSum.isPresent() && !reviewList.isEmpty()) {
            averageStar = optionalSum.get() / (double) reviewList.size();
        }


        return ProductDto.fromEntityDetail(product, isSeller, orderDtoList, userId, userName, imageUrlList, averageStar);
    }

    @Transactional(readOnly = true)
    public List<GetProductDto> getProductsByCategory(Integer pageNumber, String productCategory, String ageCategory, String genderCategory, String sortBy) {
        String inputProductCategory = ProductCategoryEnum.switchCategory(productCategory);
        if(inputProductCategory == null) throw new ProductException(ProductErrorCode.INVALID_CATEGORY);

        Pageable pageable = PageRequest.of(pageNumber - 1, 15); //한 페이지 15개
        String inputAgeCategory = AgeCategoryEnum.switchCategory(ageCategory);
        String inputGenderCategory = GenderCategoryEnum.switchCategory(genderCategory);

        if (Objects.equals(sortBy, "createdAt")) {
            return productRepositoryCustom.findByProductCategorySortByCreatedAt(inputProductCategory, inputAgeCategory, inputGenderCategory, pageable);

        }else if(Objects.equals(sortBy, "price")){
            return productRepositoryCustom.findByProductCategorySortByPrice(inputProductCategory, inputAgeCategory, inputGenderCategory, pageable);

        } else{
            return productRepositoryCustom.findByProductCategorySortById(inputProductCategory, inputAgeCategory, inputGenderCategory, pageable);

        }
    }

    @Transactional(readOnly = true)
    public List<GetProductDto> getProductList(Integer pageNumber, String ageCategory, String genderCategory, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 15); //한 페이지 15개
        String inputAgeCategory = AgeCategoryEnum.switchCategory(ageCategory);
        String inputGenderCategory = GenderCategoryEnum.switchCategory(genderCategory);

        if (Objects.equals(sortBy, "price")) {
           return productRepositoryCustom.findAllSortByPrice(inputAgeCategory, inputGenderCategory, pageable);

        } else if(Objects.equals(sortBy, "createdAt")) {
           return productRepositoryCustom.findAllSortByCreatedAt(inputAgeCategory, inputGenderCategory, pageable);

        } else {
            return productRepositoryCustom.findAllSortById(inputAgeCategory, inputGenderCategory, pageable);
        }
    }


}


