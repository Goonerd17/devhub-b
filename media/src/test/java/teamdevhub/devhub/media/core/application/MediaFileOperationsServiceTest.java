package teamdevhub.devhub.media.core.application;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.media.api.MediaUploadCommand;
import teamdevhub.devhub.media.core.port.in.command.UploadFileCommand;
import teamdevhub.devhub.media.core.port.in.usecase.FileUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MediaFileOperationsServiceTest {
    @Test
    void exposes_file_values_without_persistence_path() {
        FileUseCase useCase = mock(FileUseCase.class);
        MediaFileOperationsService service = new MediaFileOperationsService(useCase);
        FileMetadata metadata = FileMetadata.of("file-1", "image.png", "png", "/private/path", 3L);
        when(useCase.upload(any(UploadFileCommand.class))).thenReturn(metadata);
        when(useCase.selectFileObject("file-1")).thenReturn(metadata);
        when(useCase.find("file-1")).thenReturn(FileResource.of(metadata, new byte[]{1, 2, 3}));

        var uploaded = service.upload(new MediaUploadCommand("image.png", "png", 3L, new byte[]{1, 2, 3}));
        var viewed = service.metadata("file-1");
        var downloaded = service.find("file-1");

        assertThat(uploaded.fileGuid()).isEqualTo("file-1");
        assertThat(viewed.originalName()).isEqualTo("image.png");
        assertThat(viewed.size()).isEqualTo(3L);
        assertThat(downloaded.contentType()).isEqualTo("image/png");
        assertThat(downloaded.content()).containsExactly(1, 2, 3);

        service.delete("file-1");
        verify(useCase).delete("file-1");
    }
}
