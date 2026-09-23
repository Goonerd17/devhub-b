package teamdevhub.devhub.project.outbound.media;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;
import teamdevhub.devhub.project.core.port.out.MediaMetadataPort;
import teamdevhub.devhub.shared.media.*;

@FeignClient(name = "media-server", configuration = InternalFeignConfiguration.class)
public interface MediaMetadataFeignClient extends MediaMetadataPort {
    @Override @GetMapping("/internal/media/{fileGuid}/metadata")
    MediaFileMetadata findMetadata(@PathVariable String fileGuid);
}
