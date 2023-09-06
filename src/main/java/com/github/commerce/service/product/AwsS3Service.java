package com.github.commerce.service.product;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.github.commerce.service.product.exception.ProductErrorCode;
import com.github.commerce.service.product.exception.ProductException;
import com.github.commerce.service.product.util.FilePathUtils;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String memoryUpload(MultipartFile uploadFile, String fileName) throws IOException {
        try (InputStream inputStream = uploadFile.getInputStream()) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(uploadFile.getSize());
            objectMetadata.setContentType(uploadFile.getContentType());
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
            );
            return amazonS3Client.getUrl(bucket, fileName).toString();
        }
    }
    /**
     * imageUrl과 MultipartFile을 전달하면 AWS-S3에 파일을 업데이트함
     * @param multipartFile 변경할 파일
     * @param imageUrl 저장된 ImageUrl
     * @return updateUrl 경로
     * @throws IOException
     */
    public String updateFile(MultipartFile multipartFile, String imageUrl) throws IOException {
        log.info("imageUrl: {}", imageUrl);
        String filePath = FilePathUtils.convertImageUrlToFilePath(imageUrl);
        log.info("filePath: {}", filePath);

        File updateFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));

        removeFile(imageUrl);

        return putS3(updateFile, filePath);
    }

    /**
     * AWS 파일을 삭제함
     * @param imageUrl 저장된 ImageUrl 경로
     */
    private void removeFile(String imageUrl) {
        String filePath = FilePathUtils.convertImageUrlToFilePath(imageUrl);
        if(!amazonS3Client.doesObjectExist(bucket, filePath)){
            throw new ProductException(ProductErrorCode.NOT_FOUND_FILE);
        }

        amazonS3Client.deleteObject(bucket, filePath);
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
        );
        uploadFile.delete();
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private Optional<File> convert(MultipartFile file) throws  IOException {
        File convertFile = new File(file.getOriginalFilename());
        log.info("파일 이름: {}", file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        }
        log.info("File: {}", convertFile.getName());
        return Optional.of(convertFile);
    }


}
