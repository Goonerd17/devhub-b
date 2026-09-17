package teamdevhub.devhub.media.api;

public interface MediaFileOperations {
    MediaFileView upload(MediaUploadCommand command);
    MediaDownload find(String fileGuid);
    MediaFileView metadata(String fileGuid);
    void delete(String fileGuid);
}
