package teamdevhub.devhub.media.core.application;

public record FileResource(
        FileMetadata metadata,
        byte[] content
) {

    public static FileResource of(
            FileMetadata metadata,
            byte[] content
    ) {
        return new FileResource(metadata, content);
    }

    public String originalName() {
        return metadata.originalName();
    }

    public String extensionName() {
        return metadata.extensionName();
    }

    public String contentType() {
        return switch (metadata.extensionName().toLowerCase()) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
    }
}