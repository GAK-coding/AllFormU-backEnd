package gak.backend.domain.file.dto;

import gak.backend.domain.file.utils.MultiUtil;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class fileDTO {
    private String id;
    private String name;
    private String format;
    private String path;
    private long bytes;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public static fileDTO multipartOf(MultipartFile multipartFile) {
        final String fileId = MultiUtil.createFileId();
        final String format = MultiUtil.getFormat(multipartFile.getContentType());
        return fileDTO.builder()
                .id(fileId)
                .name(multipartFile.getOriginalFilename())
                .format(format)
                .path(MultiUtil.createPath(fileId, format))
                .bytes(multipartFile.getSize())
                .build();
    }
}