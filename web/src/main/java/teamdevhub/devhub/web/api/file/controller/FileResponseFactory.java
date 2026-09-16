package teamdevhub.devhub.web.api.file.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.media.api.MediaDownload;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class FileResponseFactory {

    private FileResponseFactory() {}

    public static ResponseEntity<byte[]> inline(MediaDownload fileResource) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileResource.contentType()))
                .body(fileResource.content());
    }

    public static ResponseEntity<byte[]> attachment(MediaDownload fileResource) {
        String safeName = fileResource.originalName()
                .replaceAll("[\r\n\"\\\\;]", "_");
        String encodedName = URLEncoder.encode(fileResource.originalName(), StandardCharsets.UTF_8)
                .replace("+", "%20");
        String contentDisposition = "attachment; filename=\"" + safeName + "\"; filename*=UTF-8''" + encodedName;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileResource.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(fileResource.content());
    }
}
