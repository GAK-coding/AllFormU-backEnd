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

@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile multipartFile) {
        fileDTO fileDetail = fileDTO.multipartOf(multipartFile);
        amazonS3ResourceStorage.store(fileDetail.getPath(), multipartFile);
        return generateObjectUrl(fileDetail.getPath());
    }

    private String generateObjectUrl(String path) {
        return "https://" + bucket + ".s3.amazonaws.com/" + path;
    }

}
