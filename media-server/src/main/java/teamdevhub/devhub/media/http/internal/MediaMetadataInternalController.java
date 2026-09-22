package teamdevhub.devhub.media.http.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.media.api.MediaFileMetadataQuery;
import teamdevhub.devhub.shared.media.MediaFileMetadata;

@RestController
@RequestMapping("/internal/media")
@RequiredArgsConstructor
public class MediaMetadataInternalController {
    private final MediaFileMetadataQuery metadataQuery;

    @GetMapping("/{fileGuid}/metadata")
    public MediaFileMetadata metadata(@PathVariable String fileGuid) {
        var metadata = metadataQuery.findMetadata(fileGuid);
        return new MediaFileMetadata(metadata.fileGuid(), metadata.originalName(), metadata.path());
    }
}
