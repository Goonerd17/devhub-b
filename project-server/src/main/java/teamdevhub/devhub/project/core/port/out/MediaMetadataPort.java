package teamdevhub.devhub.project.core.port.out;
import teamdevhub.devhub.shared.media.MediaFileMetadata;
public interface MediaMetadataPort { MediaFileMetadata findMetadata(String fileGuid); }
