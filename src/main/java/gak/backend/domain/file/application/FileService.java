package gak.backend.domain.file.application;

import gak.backend.domain.file.dto.fileDTO;
import gak.backend.domain.file.storage.AmazonS3ResourceStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;

    public fileDTO save(MultipartFile multipartFile) {
        fileDTO fileDetail = fileDTO.multipartOf(multipartFile);
        amazonS3ResourceStorage.store(fileDetail.getPath(), multipartFile);
        return fileDetail;
    }
}
