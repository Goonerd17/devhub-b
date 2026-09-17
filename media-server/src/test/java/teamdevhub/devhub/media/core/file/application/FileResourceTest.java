package teamdevhub.devhub.media.core.file.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.media.core.application.FileMetadata;
import teamdevhub.devhub.media.core.application.FileResource;

import static org.assertj.core.api.Assertions.assertThat;

class FileResourceTest {

    private FileMetadata metadataOf(String ext) {
        return FileMetadata.create("guid1", "file." + ext, ext, "/files/guid1", 100L);
    }

    @Test
    @DisplayName("png_확장자이면_contentType이_image/png이다")
    void contentType_png_returnsImagePng() {
        // given
        FileResource resource = FileResource.of(metadataOf("png"), new byte[0]);

        // when, then
        assertThat(resource.contentType()).isEqualTo("image/png");
    }

    @Test
    @DisplayName("jpg_확장자이면_contentType이_image/jpeg이다")
    void contentType_jpg_returnsImageJpeg() {
        // given
        FileResource resource = FileResource.of(metadataOf("jpg"), new byte[0]);

        // when, then
        assertThat(resource.contentType()).isEqualTo("image/jpeg");
    }

    @Test
    @DisplayName("jpeg_확장자이면_contentType이_image/jpeg이다")
    void contentType_jpeg_returnsImageJpeg() {
        // given
        FileResource resource = FileResource.of(metadataOf("jpeg"), new byte[0]);

        // when, then
        assertThat(resource.contentType()).isEqualTo("image/jpeg");
    }

    @Test
    @DisplayName("pdf_확장자이면_contentType이_application/pdf이다")
    void contentType_pdf_returnsApplicationPdf() {
        // given
        FileResource resource = FileResource.of(metadataOf("pdf"), new byte[0]);

        // when, then
        assertThat(resource.contentType()).isEqualTo("application/pdf");
    }

    @Test
    @DisplayName("알_수_없는_확장자이면_contentType이_application/octet-stream이다")
    void contentType_unknown_returnsApplicationOctetStream() {
        // given
        FileResource resource = FileResource.of(metadataOf("txt"), new byte[0]);

        // when, then
        assertThat(resource.contentType()).isEqualTo("application/octet-stream");
    }

    @Test
    @DisplayName("originalName과_extensionName이_metadata에서_위임된다")
    void originalNameAndExtensionName_delegatesToMetadata() {
        // given
        FileResource resource = FileResource.of(metadataOf("png"), new byte[]{1, 2, 3});

        // then
        assertThat(resource.originalName()).isEqualTo("file.png");
        assertThat(resource.extensionName()).isEqualTo("png");
        assertThat(resource.content()).isEqualTo(new byte[]{1, 2, 3});
    }
}
