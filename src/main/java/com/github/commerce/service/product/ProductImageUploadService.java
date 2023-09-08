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


    public void uploadThumbNailImage(MultipartFile thumbNailFile, Product product) {
        try {
            if (thumbNailFile != null) {
                String url = awsS3Service.memoryUpload(thumbNailFile,
                        FilePath.PRODUCT_THUMB_NAIL_DIR.getPath() + product.getId() + "/" + thumbNailFile.getOriginalFilename());
                product.setThumbnailUrl(url);
            }
        } catch (IOException e) {
            throw new ProductException(ProductErrorCode.FAIL_TO_SAVE);
        }
        //return CompletableFuture.completedFuture(null);
    }

<<<<<<< HEAD

=======
    @Async
>>>>>>> dev
    public List<String> uploadImageFileList(List<MultipartFile> imgList) {
//        List<ProductContentImage> productContentImageList =
        List<String> urlList = new ArrayList<>();
        imgList.forEach(multipartFile -> {
            String fileName = createFileName(multipartFile.getOriginalFilename());

            try {
<<<<<<< HEAD
                String url = awsS3Service.memoryUpload(multipartFile,
                        fileName);
                urlList.add(url);
//                return ProductContentImage.from(product, url);
            } catch (IOException e) {
                throw new ProductException(ProductErrorCode.FAIL_TO_SAVE);
            }

=======
                String url = awsS3Service.memoryUpload(multipartFile,uniqueIdentifier);
                urlList.add(url);
                System.out.println(5444444);
            } catch (IOException e) {
                throw new ProductException(ProductErrorCode.FAIL_TO_SAVE);
            }
>>>>>>> dev
        });
        return urlList;
//                .collect(Collectors.toList());
//        productContentImageRepository.saveAll(productContentImageList);

        //return CompletableFuture.completedFuture(null);
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        try {
            String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            if (extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg")) {
                return extension;
            }
            throw new ProductException(ProductErrorCode.NOT_IMAGE_EXTENSION);
        } catch (StringIndexOutOfBoundsException e) {
            throw new ProductException(ProductErrorCode.INVALID_FORMAT_FILE);
        }
    }


}
