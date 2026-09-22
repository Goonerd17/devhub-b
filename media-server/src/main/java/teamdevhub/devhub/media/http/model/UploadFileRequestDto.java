package teamdevhub.devhub.media.http.model;

import org.springframework.web.multipart.MultipartFile;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.media.api.MediaUploadCommand;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public record UploadFileRequestDto(
        Map<String, MultipartFile> files
) {

    public static UploadFileRequestDto from(Map<String, MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw BusinessRuleException.of(ErrorCode.FILE_EMPTY);
        }
        return new UploadFileRequestDto(files);
    }

    public Map<String, MediaUploadCommand> toCommandMap() {
        return files.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> toCommand(entry.getValue())
                ));
    }

    private MediaUploadCommand toCommand(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String extension = extractExtension(originalName);

            return new MediaUploadCommand(
                    originalName,
                    extension,
                    file.getSize(),
                    file.getBytes()
            );

        } catch (IOException e) {
            throw BusinessRuleException.of(ErrorCode.FILE_READ_FAIL);
        }
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
