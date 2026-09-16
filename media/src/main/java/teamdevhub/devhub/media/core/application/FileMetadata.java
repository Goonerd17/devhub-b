package teamdevhub.devhub.media.core.application;

import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

public record FileMetadata(
        String fileGuid,
        String originalName,
        String extensionName,
        String path,
        long size
) {

    public static FileMetadata create(
            String fileGuid,
            String originalName,
            String extensionName,
            String path,
            long size
    ) {
        validate(originalName, extensionName, size);
        return new FileMetadata(fileGuid, originalName, extensionName, path, size);
    }

    public static FileMetadata of(
            String fileGuid,
            String originalName,
            String extensionName,
            String path,
            long size
    ) {
        return new FileMetadata(fileGuid, originalName, extensionName, path, size);
    }

    private static void validate(
            String originalName,
            String extensionName,
            long size
    ) {
        if (originalName == null || originalName.isBlank()) {
            throw BusinessRuleException.of(ErrorCode.FILE_NAME_REQUIRED);
        }

        if (extensionName == null || extensionName.isBlank()) {
            throw BusinessRuleException.of(ErrorCode.FILE_EXTENSION_REQUIRED);
        }

        if (size <= 0) {
            throw BusinessRuleException.of(ErrorCode.FILE_SIZE_INVALID);
        }
    }
}
