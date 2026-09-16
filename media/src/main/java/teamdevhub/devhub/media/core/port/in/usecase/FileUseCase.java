package teamdevhub.devhub.media.core.port.in.usecase;

import teamdevhub.devhub.media.core.application.FileResource;
import teamdevhub.devhub.media.core.application.FileMetadata;
import teamdevhub.devhub.media.core.port.in.command.UploadFileCommand;

public interface FileUseCase {

    FileMetadata upload(UploadFileCommand uploadFileCommand);
    FileResource find(String fileGuid);
    void delete(String fileId);

    FileMetadata selectFileObject(String fileGuid);
}
