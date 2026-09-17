package teamdevhub.devhub.media.core.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.shared.identifier.IdentifierProvider;
import teamdevhub.devhub.media.api.MediaFileMetadata;
import teamdevhub.devhub.media.api.MediaFileMetadataQuery;
import teamdevhub.devhub.media.core.port.in.command.UploadFileCommand;
import teamdevhub.devhub.media.core.port.in.usecase.FileUseCase;
import teamdevhub.devhub.media.core.port.out.FileMetadataRepository;
import teamdevhub.devhub.media.core.port.out.FileStorage;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService implements FileUseCase, MediaFileMetadataQuery {

    @Override
    public MediaFileMetadata findMetadata(String fileGuid) {
        FileMetadata metadata = selectFileObject(fileGuid);
        return new MediaFileMetadata(metadata.fileGuid(), metadata.originalName(), metadata.path());
    }

    private final IdentifierProvider identifierProvider;
    private final FileStorage fileStorage;
    private final FileMetadataRepository fileMetadataRepository;

    @Override
    public FileMetadata upload(UploadFileCommand uploadFileCommand) {
        String fileGuid = identifierProvider.generateIdentifier();
        String path = fileStorage.save(fileGuid, uploadFileCommand.content());
        FileMetadata fileMetadata = FileMetadata.create(
                fileGuid,
                uploadFileCommand.originalName(),
                uploadFileCommand.extension(),
                path,
                uploadFileCommand.size()
        );

        return fileMetadataRepository.save(fileMetadata);
    }

    @Override
    public FileResource find(String fileGuid) {
        FileMetadata fileMetadata = fileMetadataRepository.find(fileGuid);
        byte[] content = fileStorage.read(fileGuid);
        return FileResource.of(fileMetadata, content);
    }

    @Override
    public void delete(String fileGuid) {
        fileMetadataRepository.deleteByFileGuid(fileGuid);
        fileStorage.delete(fileGuid);
    }

    @Override
    public FileMetadata selectFileObject(String fileGuid) {
        return fileMetadataRepository.find(fileGuid);
    }
}
