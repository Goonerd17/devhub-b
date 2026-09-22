package teamdevhub.devhub.media.http.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.shared.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.media.api.MediaDownload;
import teamdevhub.devhub.media.api.MediaUploadCommand;
import teamdevhub.devhub.media.http.facade.FileFacade;
import teamdevhub.devhub.media.http.facade.model.FileResponseDto;
import teamdevhub.devhub.media.http.facade.model.UploadFileResponseDto;
import teamdevhub.devhub.media.http.fake.FakeFileUseCase;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileFacadeTest {

    private FileFacade fileFacade;

    @BeforeEach
    void init() {
        FakeFileUseCase fakeFileUseCase = new FakeFileUseCase();
        fileFacade = new FileFacade(fakeFileUseCase);
    }

    private String uploadSingle(String name) {
        MediaUploadCommand command = new MediaUploadCommand(name + ".txt", "txt", 4L, "data".getBytes());
        return fileFacade.upload(Map.of("file", command)).fileGuids().get("file");
    }

    @Test
    @DisplayName("?뚯씪_?ㅼ쨷_?낅줈???뚯뒪??")
    void upload_multiple_files() {
        // given
        MediaUploadCommand command1 = new MediaUploadCommand("a.txt", "txt", 4L, "data".getBytes());
        MediaUploadCommand command2 = new MediaUploadCommand("b.txt", "txt", 4L, "data".getBytes());

        Map<String, MediaUploadCommand> commands = Map.of(
                "profileImage", command1,
                "resume", command2
        );

        // when
        UploadFileResponseDto response = fileFacade.upload(commands);

        // then
        assertThat(response.fileGuids()).hasSize(2);
        assertThat(response.fileGuids()).containsKeys("profileImage", "resume");
    }

    @Test
    @DisplayName("?뚯씪_議고쉶_?뚯뒪??")
    void find_test() {
        // given
        String guid = uploadSingle("a");

        // when
        MediaDownload resource = fileFacade.find(guid);

        // then
        assertThat(resource.originalName()).isEqualTo("a.txt");
        assertThat(resource.content()).isEqualTo("data".getBytes());
    }

    @Test
    @DisplayName("議댁옱?섏?_?딅뒗_?뚯씪??議고쉶?섎㈃_AdapterDataException??諛쒖깮?쒕떎")
    void find_nonExistent_throwsAdapterDataException() {
        // when, then
        assertThatThrownBy(() -> fileFacade.find("nonExistentGuid"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());
    }

    @Test
    @DisplayName("?뚯씪_??젣_??議고쉶?섎㈃_AdapterDataException??諛쒖깮?쒕떎")
    void delete_thenFind_throwsAdapterDataException() {
        // given
        String guid = uploadSingle("a");

        // when
        fileFacade.delete(guid);

        // then
        assertThatThrownBy(() -> fileFacade.find(guid))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());
    }

    @Test
    @DisplayName("?뚯씪_硫뷀??곗씠?곕?_議고쉶?섎㈃_?뚯씪?뺣낫瑜?諛섑솚?쒕떎")
    void selectFileObject_returnsFileResponseDto() {
        // given
        String guid = uploadSingle("a");

        // when
        FileResponseDto result = fileFacade.selectFileObject(guid);

        // then
        assertThat(result.fileGuid()).isEqualTo(guid);
        assertThat(result.filename()).isEqualTo("a.txt");
        assertThat(result.size()).isEqualTo(4L);
    }

    @Test
    @DisplayName("議댁옱?섏?_?딅뒗_?뚯씪??硫뷀??곗씠??議고쉶??AdapterDataException??諛쒖깮?쒕떎")
    void selectFileObject_nonExistent_throwsAdapterDataException() {
        // when, then
        assertThatThrownBy(() -> fileFacade.selectFileObject("nonExistentGuid"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());
    }
}
