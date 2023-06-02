package gak.backend.domain.file.application;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import gak.backend.domain.file.dto.fileDTO;
import gak.backend.domain.file.storage.AmazonS3ResourceStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;
    private final AmazonS3 amazonS3;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public fileDTO getImageInfo(MultipartFile multipartFile) {
        fileDTO fileDetail = fileDTO.multipartOf(multipartFile);
        amazonS3ResourceStorage.store(fileDetail.getPath(), multipartFile);
        return fileDetail;
    }
    public String uploadFile(MultipartFile multipartFile) {
        fileDTO fileDetail = fileDTO.multipartOf(multipartFile);
        amazonS3ResourceStorage.store(fileDetail.getPath(), multipartFile);
        return generateObjectUrl(fileDetail.getPath());
    }

    private String generateObjectUrl(String path) {
        return "https://" + bucket + ".s3.amazonaws.com/" + path;
    }

    public void deleteImageByUrl(String imageUrl) {
        String fileName = extractFileNameFromUrl(imageUrl);
        if (fileName != null) {
            amazonS3.deleteObject(bucket, fileName);
        } else {
            throw new IllegalArgumentException("Invalid image URL");
        }
    }

    private String extractFileNameFromUrl(String imageUrl) {
        try {
            URI uri = new URI(imageUrl);
            String path = uri.getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid image URL", e);
        }
    }

}
