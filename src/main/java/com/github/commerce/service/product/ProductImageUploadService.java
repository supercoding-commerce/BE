package com.github.commerce.service.product;

import com.github.commerce.entity.Product;
import com.github.commerce.entity.ProductContentImage;
import com.github.commerce.repository.product.ProductContentImageRepository;
import com.github.commerce.service.product.exception.ProductErrorCode;
import com.github.commerce.service.product.exception.ProductException;
import com.github.commerce.service.product.util.FilePath;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductImageUploadService {

    private final AwsS3Service awsS3Service;
    private final ProductContentImageRepository productContentImageRepository;

    @Async
    public CompletableFuture<Void> uploadThumbNailImage(MultipartFile thumbNailFile, Product product) {
        try {
            if (thumbNailFile != null) {
                String url = awsS3Service.memoryUpload(thumbNailFile,
                        FilePath.PRODUCT_THUMB_NAIL_DIR.getPath() + product.getId() + "/" + thumbNailFile.getOriginalFilename());
                product.setThumbnailUrl(url);
            }
        } catch (IOException e) {
            throw new ProductException(ProductErrorCode.FAIL_TO_SAVE);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> uploadImageFileList(List<MultipartFile> imgList, Product product) {
//        List<ProductContentImage> productContentImageList =
        List<String> urlList = new ArrayList<>();
        imgList.forEach(multipartFile -> {
            String uniqueIdentifier = UUID.randomUUID().toString();
            try {
                String url = awsS3Service.memoryUpload(multipartFile,
                        FilePath.PRODUCT_CONTENT_DIR.getPath() + uniqueIdentifier);
//                return ProductContentImage.from(product, url);
            } catch (IOException e) {
                throw new ProductException(ProductErrorCode.FAIL_TO_SAVE);
            }
            urlList.add(FilePath.PRODUCT_CONTENT_DIR.getPath() + uniqueIdentifier);
        });
//                .collect(Collectors.toList());
//        productContentImageRepository.saveAll(productContentImageList);

        return CompletableFuture.completedFuture(null);
    }

}
