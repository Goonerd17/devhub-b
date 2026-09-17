package teamdevhub.devhub.media.http.facade.model;

import teamdevhub.devhub.media.api.MediaFileView;

public record FileResponseDto(String fileGuid, String filename, long size) {
    public static FileResponseDto from(MediaFileView metadata) {
        return new FileResponseDto(metadata.fileGuid(), metadata.originalName(), metadata.size());
    }
}
