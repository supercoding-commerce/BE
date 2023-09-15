package com.github.commerce.service.product;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.ProductContentImage;
import com.github.commerce.repository.product.ProductContentImageRepository;
import com.github.commerce.service.product.exception.ProductErrorCode;
import com.github.commerce.service.product.exception.ProductException;
import com.github.commerce.service.product.util.FilePath;
import com.github.commerce.service.review.exception.ReviewErrorCode;
import com.github.commerce.service.review.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ProductImageUploadService {

    private final AwsS3Service awsS3Service;
    private final ProductContentImageRepository productContentImageRepository;


    public String uploadReviewImage(MultipartFile multipartFile) {
        String fileName = createFileName(multipartFile.getOriginalFilename());
        try {
            if (multipartFile.isEmpty()) {
                throw new ReviewException(ReviewErrorCode.IMAGE_EMPTY);
            }

            return awsS3Service.memoryUpload(multipartFile,
                    FilePath.REVIEW_IMG_DIR.getPath() + fileName);

        } catch (IOException e) {
            throw new ReviewException(ReviewErrorCode.FAILED_UPLOAD);
        }
    }

    public String uploadShopImage(MultipartFile shopImgFile) {
        String fileName = createFileName(shopImgFile.getOriginalFilename());
        try {
            if (shopImgFile.isEmpty()) {
                throw new ReviewException(ReviewErrorCode.IMAGE_EMPTY);
            }

            return awsS3Service.memoryUpload(shopImgFile,
                    FilePath.SHOP_IMG_DIR.getPath() + fileName);

        } catch (IOException e) {
            throw new ReviewException(ReviewErrorCode.FAILED_UPLOAD);
        }
    }

    public List<String> uploadImageFileList(List<MultipartFile> imgList) {
        List<String> urlList = new ArrayList<>();
        imgList.forEach(multipartFile -> {
            String fileName = createFileName(multipartFile.getOriginalFilename());

            try {
                String url = awsS3Service.memoryUpload(multipartFile,
                        fileName);
                urlList.add(url);
            } catch (IOException e) {
                throw new ProductException(ProductErrorCode.FAIL_TO_SAVE);
            }
        });
        return urlList;
}

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        try {
            String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            if (extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".webp")) {
                return extension;
            }
            throw new ProductException(ProductErrorCode.NOT_IMAGE_EXTENSION);
        } catch (StringIndexOutOfBoundsException e) {
            throw new ProductException(ProductErrorCode.INVALID_FORMAT_FILE);
        }
    }



}
