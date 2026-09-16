package teamdevhub.devhub.media.outbound.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.media.core.application.FileMetadata;
import teamdevhub.devhub.media.core.port.out.FileMetadataRepository;
import teamdevhub.devhub.platform.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.media.outbound.adapter.entity.FileEntity;
import teamdevhub.devhub.media.outbound.adapter.mapper.FileMetadataMapper;
import teamdevhub.devhub.media.outbound.persistence.JpaFileRepository;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

@Component
@RequiredArgsConstructor
public class FileMetadataAdapter implements FileMetadataRepository {

    private final JpaFileRepository jpaFileRepository;

    @Override
    public FileMetadata save(FileMetadata fileMetadata) {
        FileEntity fileEntity = jpaFileRepository.save(FileMetadataMapper.toEntity(fileMetadata));
        return FileMetadataMapper.toDomain(fileEntity);
    }

    @Override
    public FileMetadata find(String fileGuid) {
        return jpaFileRepository.findByFileGuid(fileGuid)
                .map(FileMetadataMapper::toDomain)
                .orElseThrow(() -> AdapterDataException.of(ErrorCode.FILE_READ_FAIL));
    }

    @Override
    public void deleteByFileGuid(String fileGuid) {
        jpaFileRepository.deleteByFileGuid(fileGuid);
    }
}
