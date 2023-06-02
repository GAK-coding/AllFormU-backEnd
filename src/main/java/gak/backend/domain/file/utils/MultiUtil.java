package gak.backend.domain.file.utils;

import org.springframework.util.StringUtils;

import java.util.UUID;

public class MultiUtil {

    public static String getLocalHomeDirectory() {
        return System.getProperty("user.home");
    }

    public static String createFileId() {
        return UUID.randomUUID().toString();
    }

    public static String getFormat(String contentType) {
        if (StringUtils.hasText(contentType)) {
            return contentType.substring(contentType.lastIndexOf('/') + 1);
        }
        return null;
    }

    public static String createPath(String fileId, String format) {
        return String.format("%s.%s", fileId, format);
    }
}

