package teamdevhub.devhub.media.core.port.out;

import teamdevhub.devhub.media.core.application.FileMetadata;

public interface FileMetadataRepository {

    FileMetadata save(FileMetadata fileMetadata);
    FileMetadata find(String fileGuid);
    void deleteByFileGuid(String fileGuid);
}
