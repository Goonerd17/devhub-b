package teamdevhub.devhub.media.http.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.media.api.MediaDownload;
import teamdevhub.devhub.media.api.MediaFileOperations;
import teamdevhub.devhub.media.api.MediaUploadCommand;
import teamdevhub.devhub.media.http.facade.model.FileResponseDto;
import teamdevhub.devhub.media.http.facade.model.UploadFileResponseDto;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FileFacade {

    private final MediaFileOperations mediaFileOperations;

    public UploadFileResponseDto upload(Map<String, MediaUploadCommand> commands) {
        return UploadFileResponseDto.from(
                commands.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> mediaFileOperations.upload(entry.getValue()).fileGuid()
                        ))
        );
    }

    public MediaDownload find(String fileGuid) {
        return mediaFileOperations.find(fileGuid);
    }

    public void delete(String fileGuid) {
        mediaFileOperations.delete(fileGuid);
    }

    public FileResponseDto selectFileObject(String fileGuid) {
        return FileResponseDto.from(mediaFileOperations.metadata(fileGuid));
    }
}
