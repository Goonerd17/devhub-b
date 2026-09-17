package teamdevhub.devhub.media.http.facade.model;

import java.util.Map;

public record UploadFileResponseDto(Map<String, String> fileGuids) {
    public static UploadFileResponseDto from(Map<String, String> fileGuids) {
        return new UploadFileResponseDto(Map.copyOf(fileGuids));
    }
}
