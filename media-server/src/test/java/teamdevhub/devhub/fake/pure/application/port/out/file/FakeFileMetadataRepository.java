package teamdevhub.devhub.fake.pure.application.port.out.file;

import teamdevhub.devhub.shared.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.media.core.application.FileMetadata;
import teamdevhub.devhub.media.core.port.out.FileMetadataRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeFileMetadataRepository implements FileMetadataRepository {

    private final Map<String, FileMetadata> store = new HashMap<>();

    @Override
    public FileMetadata save(FileMetadata fileMetadata) {
        store.put(fileMetadata.fileGuid(), fileMetadata);
        return fileMetadata;
    }

    @Override
    public FileMetadata find(String fileGuid) {
        FileMetadata metadata = store.get(fileGuid);
        if (metadata == null) {
            throw AdapterDataException.of(ErrorCode.FILE_READ_FAIL);
        }
        return metadata;
    }

    @Override
    public void deleteByFileGuid(String fileGuid) {
        store.remove(fileGuid);
    }

    public Optional<FileMetadata> findByFileGuid(String fileGuid) {
        return Optional.ofNullable(store.get(fileGuid));
    }
}
