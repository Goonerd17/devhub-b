package teamdevhub.devhub.media.api;

public record MediaUploadCommand(String originalName, String extension, long size, byte[] content) {
}
