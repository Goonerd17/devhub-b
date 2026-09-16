package teamdevhub.devhub.web.api.file.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.web.api.file.controller.FileController;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.media.api.MediaDownload;
import teamdevhub.devhub.web.core.file.port.in.facade.FileFacade;
import teamdevhub.devhub.web.core.file.port.in.facade.model.FileResponseDto;
import teamdevhub.devhub.web.core.file.port.in.facade.model.UploadFileResponseDto;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileControllerUnitTest {

    private FileController fileController;
    private FileFacade fileFacade;

    @BeforeEach
    void init() {
        fileFacade = Mockito.mock(FileFacade.class);
        fileController = new FileController(fileFacade);
    }

    @Test
    @DisplayName("파일_메타데이터_조회에_성공하면_READ_SUCCESS_코드를_반환한다")
    void selectFile_returnsReadSuccess() {
        // given
        String fileGuid = "FILE_GUID_001";
        FileResponseDto responseDto = new FileResponseDto(fileGuid, "image.png", 1024L);
        when(fileFacade.selectFileObject(fileGuid)).thenReturn(responseDto);

        // when
        ResponseEntity<DataApiResponseDto<FileResponseDto>> response = fileController.selectFile(fileGuid);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
        assertThat(response.getBody().getData().fileGuid()).isEqualTo(fileGuid);
        assertThat(response.getBody().getData().filename()).isEqualTo("image.png");
        verify(fileFacade).selectFileObject(fileGuid);
    }

    @Test
    @DisplayName("파일_삭제에_성공하면_DELETE_SUCCESS_코드를_반환한다")
    void delete_returnsDeleteSuccess() {
        // given
        String fileGuid = "FILE_GUID_002";
        doNothing().when(fileFacade).delete(fileGuid);

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = fileController.delete(fileGuid);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.DELETE_SUCCESS.getCode());
        verify(fileFacade).delete(fileGuid);
    }

    @Test
    @DisplayName("파일_인라인_조회에_성공하면_파일_내용을_바이트로_반환한다")
    void view_returnsFileContent() {
        // given
        String fileGuid = "FILE_GUID_003";
        MediaDownload fileResource = new MediaDownload("image.png", "image/png", new byte[]{1, 2, 3});
        when(fileFacade.find(fileGuid)).thenReturn(fileResource);

        // when
        ResponseEntity<byte[]> response = fileController.view(fileGuid);

        // then
        assertThat(response.getBody()).isEqualTo(new byte[]{1, 2, 3});
        assertThat(response.getHeaders().getContentType()).isNotNull();
        verify(fileFacade).find(fileGuid);
    }

    @Test
    @DisplayName("파일_다운로드에_성공하면_Content-Disposition_헤더가_포함된다")
    void download_includesContentDispositionHeader() {
        // given
        String fileGuid = "FILE_GUID_004";
        MediaDownload fileResource = new MediaDownload("document.pdf", "application/pdf", new byte[]{7, 8, 9});
        when(fileFacade.find(fileGuid)).thenReturn(fileResource);

        // when
        ResponseEntity<byte[]> response = fileController.download(fileGuid);

        // then
        assertThat(response.getBody()).isEqualTo(new byte[]{7, 8, 9});
        assertThat(response.getHeaders().getFirst("Content-Disposition")).contains("attachment");
        verify(fileFacade).find(fileGuid);
    }

    @Test
    @DisplayName("파일_업로드에_성공하면_CREATE_SUCCESS_코드와_파일_GUID_맵을_반환한다")
    void upload_returnsCreateSuccessWithFileGuids() {
        // given
        UploadFileResponseDto uploadResponse = UploadFileResponseDto.from(Map.of("profileImage", "FILE_GUID_005"));
        when(fileFacade.upload(any())).thenReturn(uploadResponse);

        org.springframework.mock.web.MockMultipartFile multipartFile =
                new org.springframework.mock.web.MockMultipartFile(
                        "profileImage",
                        "test.png",
                        "image/png",
                        new byte[]{1, 2, 3}
                );
        Map<String, org.springframework.web.multipart.MultipartFile> uploadFiles = Map.of("profileImage", multipartFile);

        // when
        ResponseEntity<DataApiResponseDto<UploadFileResponseDto>> response = fileController.upload(uploadFiles);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.CREATE_SUCCESS.getCode());
        assertThat(response.getBody().getData().fileGuids()).containsKey("profileImage");
        verify(fileFacade).upload(any());
    }
}
