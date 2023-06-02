package gak.backend.domain.file.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import gak.backend.domain.file.exception.StorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AmazonS3ResourceStorage {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void store(String fullPath, MultipartFile multipartFile) {
        File file = null;
        try {
            file = convertMultipartFileToFile(multipartFile);
            uploadFileToS3(fullPath, file);
        } finally {
            if (file != null) {
                deleteLocalFile(file);
            }
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        try {
            File convertedFile = File.createTempFile("temp-", null);
            multipartFile.transferTo(convertedFile);
            return convertedFile;
        } catch (IOException e) {
            throw new StorageException("Failed to convert multipart file to file", e);
        }
    }

    private void uploadFileToS3(String fullPath, File file) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fullPath, file);

            // Content-Disposition 헤더 설정
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentDisposition("inline");
            putObjectRequest.setMetadata(objectMetadata);

            // 공개 액세스 설정
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(putObjectRequest);
        } catch (SdkClientException e) {
            throw new StorageException("Failed to upload file to Amazon S3", e);
        }
    }



    private void deleteLocalFile(File file) {
        if (!file.delete()) {
            throw new StorageException("Failed to delete local file");
        }
    }

}