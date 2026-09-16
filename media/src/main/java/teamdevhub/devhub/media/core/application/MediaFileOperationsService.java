package teamdevhub.devhub.media.core.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.media.api.MediaDownload;
import teamdevhub.devhub.media.api.MediaFileOperations;
import teamdevhub.devhub.media.api.MediaFileView;
import teamdevhub.devhub.media.api.MediaUploadCommand;
import teamdevhub.devhub.media.core.port.in.command.UploadFileCommand;
import teamdevhub.devhub.media.core.port.in.usecase.FileUseCase;

@Service
@RequiredArgsConstructor
public class MediaFileOperationsService implements MediaFileOperations {
    private final FileUseCase fileUseCase;

    @Override
    public MediaFileView upload(MediaUploadCommand command) {
        return toView(fileUseCase.upload(new UploadFileCommand(
                command.originalName(), command.extension(), command.size(), command.content())));
    }

    @Override
    public MediaDownload find(String fileGuid) {
        FileResource resource = fileUseCase.find(fileGuid);
        return new MediaDownload(resource.originalName(), resource.contentType(), resource.content());
    }

    @Override
    public MediaFileView metadata(String fileGuid) {
        return toView(fileUseCase.selectFileObject(fileGuid));
    }

    @Override
    public void delete(String fileGuid) {
        fileUseCase.delete(fileGuid);
    }

    private MediaFileView toView(FileMetadata metadata) {
        return new MediaFileView(metadata.fileGuid(), metadata.originalName(), metadata.size());
    }
}
