package teamdevhub.devhub.media.core.port.in.command;

public record UploadFileCommand(
        String originalName,
        String extension,
        long size,
        byte[] content
) {

}
