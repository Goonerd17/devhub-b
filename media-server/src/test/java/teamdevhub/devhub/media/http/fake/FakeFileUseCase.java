package teamdevhub.devhub.media.http.fake;

import teamdevhub.devhub.shared.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.media.api.MediaDownload;
import teamdevhub.devhub.media.api.MediaFileOperations;
import teamdevhub.devhub.media.api.MediaFileView;
import teamdevhub.devhub.media.api.MediaUploadCommand;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;

import java.util.HashMap;
import java.util.Map;

public class FakeFileUseCase implements MediaFileOperations {

    private final Map<String, MediaFileView> metadataStore = new HashMap<>();
    private final Map<String, byte[]> contentStore = new HashMap<>();

    private int sequence = 1;

    @Override
    public MediaFileView upload(MediaUploadCommand command) {
        String guid = "fake-file-" + sequence++;

        MediaFileView metadata = new MediaFileView(guid, command.originalName(), command.size());

        metadataStore.put(guid, metadata);
        contentStore.put(guid, command.content());

        return metadata;
    }

    @Override
    public MediaDownload find(String fileGuid) {
        MediaFileView metadata = metadataStore.get(fileGuid);
        if (metadata == null) {
            throw AdapterDataException.of(ErrorCode.FILE_READ_FAIL);
        }
        String extension = metadata.originalName().substring(metadata.originalName().lastIndexOf('.') + 1);
        String contentType = switch (extension.toLowerCase()) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
        return new MediaDownload(metadata.originalName(), contentType, contentStore.get(fileGuid));
    }

    @Override
    public void delete(String fileGuid) {
        metadataStore.remove(fileGuid);
        contentStore.remove(fileGuid);
    }

    @Override
    public MediaFileView metadata(String fileGuid) {
        MediaFileView metadata = metadataStore.get(fileGuid);
        if (metadata == null) {
            throw AdapterDataException.of(ErrorCode.FILE_READ_FAIL);
        }
        return metadata;
    }
}
