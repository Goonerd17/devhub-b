package teamdevhub.devhub.media.outbound.adapter.mapper;

import teamdevhub.devhub.media.core.application.FileMetadata;
import teamdevhub.devhub.media.outbound.adapter.entity.FileEntity;

public final class FileMetadataMapper {

    private FileMetadataMapper() {
    }

    public static FileEntity toEntity(FileMetadata fileMetadata) {
        return FileEntity.builder()
                .fileGuid(fileMetadata.fileGuid())
                .originalName(fileMetadata.originalName())
                .extensionName(fileMetadata.extensionName())
                .path(fileMetadata.path())
                .size(fileMetadata.size())
                .build();
    }

    public static FileMetadata toDomain(FileEntity fileEntity) {
        return FileMetadata.of(
                fileEntity.getFileGuid(),
                fileEntity.getOriginalName(),
                fileEntity.getExtensionName(),
                fileEntity.getPath(),
                fileEntity.getSize()
        );
    }
}