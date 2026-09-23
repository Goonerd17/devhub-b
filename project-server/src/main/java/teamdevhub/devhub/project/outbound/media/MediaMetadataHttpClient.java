package teamdevhub.devhub.project.outbound.media;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.shared.media.MediaFileMetadata;
import teamdevhub.devhub.project.core.port.out.MediaMetadataPort;

@Component
public class MediaMetadataHttpClient implements MediaMetadataPort {
    private final MediaMetadataFeignClient client;

    public MediaMetadataHttpClient(MediaMetadataFeignClient client) {
        this.client = client;
    }

    @Override
    public MediaFileMetadata findMetadata(String fileGuid) {
        return client.findMetadata(fileGuid);
    }
}
