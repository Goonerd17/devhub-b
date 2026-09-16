package teamdevhub.devhub.web.api.file.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.web.api.file.controller.FileResponseFactory;
import teamdevhub.devhub.media.api.MediaDownload;

import static org.assertj.core.api.Assertions.assertThat;

class FileResponseFactoryTest {

    private MediaDownload pngResource() {
        return new MediaDownload("image.png", "image/png", new byte[]{1, 2, 3});
    }

    private MediaDownload textResource() {
        return new MediaDownload("doc.txt", "application/octet-stream", new byte[]{4, 5, 6});
    }

    @Test
    @DisplayName("inline_?묐떟?_200_?곹깭肄붾뱶?_?뚯씪_?댁슜??諛섑솚?쒕떎")
    void inline_returnsWith200AndContent() {
        // given
        MediaDownload resource = pngResource();

        // when
        ResponseEntity<byte[]> response = FileResponseFactory.inline(resource);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new byte[]{1, 2, 3});
        assertThat(response.getHeaders().getContentType()).isNotNull();
        assertThat(response.getHeaders().getContentType().toString()).isEqualTo("image/png");
    }

    @Test
    @DisplayName("attachment_?묐떟?_Content-Disposition_?ㅻ뜑??filename???ы븿?쒕떎")
    void attachment_includesContentDispositionHeader() {
        // given
        MediaDownload resource = pngResource();

        // when
        ResponseEntity<byte[]> response = FileResponseFactory.attachment(resource);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new byte[]{1, 2, 3});

        String contentDisposition = response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
        assertThat(contentDisposition).isNotNull();
        assertThat(contentDisposition).contains("attachment");
        assertThat(contentDisposition).contains("filename=");
        assertThat(contentDisposition).contains("image.png");
    }

    @Test
    @DisplayName("attachment_?묐떟??Content-Disposition???뱀닔臾몄옄媛_?덉쑝硫??몃뜑?ㅼ퐫?대줈_?泥대맂??")
    void attachment_sanitizesSpecialCharsInFilename() {
        // given
        MediaDownload resource = new MediaDownload("file\r\nname.txt", "application/octet-stream", new byte[]{1});

        // when
        ResponseEntity<byte[]> response = FileResponseFactory.attachment(resource);

        // then
        String contentDisposition = response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
        assertThat(contentDisposition).isNotNull();
        assertThat(contentDisposition).doesNotContain("\r");
        assertThat(contentDisposition).doesNotContain("\n");
    }

    @Test
    @DisplayName("txt_?뚯씪??inline_?묐떟_ContentType?_application/octet-stream?대떎")
    void inline_txtFile_returnsOctetStream() {
        // given
        MediaDownload resource = textResource();

        // when
        ResponseEntity<byte[]> response = FileResponseFactory.inline(resource);

        // then
        assertThat(response.getHeaders().getContentType()).isNotNull();
        assertThat(response.getHeaders().getContentType().toString()).isEqualTo("application/octet-stream");
    }
}
