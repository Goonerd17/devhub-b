package teamdevhub.devhub.project.outbound.media;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.media.MediaFileMetadata;
import teamdevhub.devhub.shared.media.MediaFileMetadataQuery;

@Component
public class MediaMetadataHttpClient implements MediaFileMetadataQuery {
    private final RestClient client;

    public MediaMetadataHttpClient(RestClient.Builder builder,
            @Value("${services.media.base-url:http://media-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @Override
    public MediaFileMetadata findMetadata(String fileGuid) {
        return client.get()
                .uri("/internal/media/{fileGuid}/metadata", fileGuid)
                .retrieve()
                .body(MediaFileMetadata.class);
    }
}
