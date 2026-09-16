package teamdevhub.devhub.media.core.file.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.media.core.application.FileMetadata;
import teamdevhub.devhub.media.core.application.FileResource;
import teamdevhub.devhub.media.core.application.FileService;
import teamdevhub.devhub.media.core.port.in.command.UploadFileCommand;
import teamdevhub.devhub.fake.pure.application.port.out.file.FakeFileMetadataRepository;
import teamdevhub.devhub.fake.pure.application.port.out.file.FakeFileStorage;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileServiceTest {

    private FileService fileService;

    private FakeFileStorage fileStorage;
    private FakeFileMetadataRepository fileMetadataRepository;

    @BeforeEach
    void init() {
        FakeUuidIdentifierProvider identifierProvider =
                new FakeUuidIdentifierProvider("TestFileGuid1");

        fileStorage = new FakeFileStorage();
        fileMetadataRepository = new FakeFileMetadataRepository();

        fileService = new FileService(
                identifierProvider,
                fileStorage,
                fileMetadataRepository
        );
    }

    private UploadFileCommand textFileCommand() {
        return new UploadFileCommand("test.txt", "txt", 4L, "data".getBytes());
    }

    @Test
    @DisplayName("?뚯씪_?낅줈???뚯뒪??")
    void upload_with_fake() {
        // when
        FileMetadata metadata = fileService.upload(textFileCommand());

        // then
        assertThat(metadata.fileGuid()).isEqualTo("TestFileGuid1");
        assertThat(metadata.originalName()).isEqualTo("test.txt");
        assertThat(metadata.extensionName()).isEqualTo("txt");
        assertThat(metadata.size()).isEqualTo(4L);

        assertThat(fileStorage.exists("TestFileGuid1")).isTrue();
        assertThat(fileMetadataRepository.find("TestFileGuid1")).isNotNull();
    }

    @Test
    @DisplayName("?뚯씪_議고쉶_?뚯뒪??")
    void find_with_fake() {
        // given
        fileService.upload(textFileCommand());

        // when
        FileResource resource = fileService.find("TestFileGuid1");

        // then
        assertThat(resource.metadata().fileGuid()).isEqualTo("TestFileGuid1");
        assertThat(resource.content()).isEqualTo("data".getBytes());
    }

    @Test
    @DisplayName("議댁옱?섏?_?딅뒗_?뚯씪??議고쉶?섎㈃_AdapterDataException??諛쒖깮?쒕떎")
    void find_nonExistent_throwsAdapterDataException() {
        // when, then
        assertThatThrownBy(() -> fileService.find("nonExistentGuid"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());
    }

    @Test
    @DisplayName("?뚯씪_??젣_??議고쉶?섎㈃_AdapterDataException??諛쒖깮?쒕떎")
    void delete_thenFind_throwsAdapterDataException() {
        // given
        fileService.upload(textFileCommand());

        // when
        fileService.delete("TestFileGuid1");

        // then
        assertThatThrownBy(() -> fileService.find("TestFileGuid1"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());

        assertThat(fileStorage.exists("TestFileGuid1")).isFalse();
    }

    @Test
    @DisplayName("?뚯씪_硫뷀??곗씠?곕?_議고쉶?섎㈃_?뚯씪?뺣낫瑜?諛섑솚?쒕떎")
    void selectFileObject_returnsFileResponseDto() {
        // given
        fileService.upload(textFileCommand());

        // when
        FileMetadata result = fileService.selectFileObject("TestFileGuid1");

        // then
        assertThat(result.fileGuid()).isEqualTo("TestFileGuid1");
        assertThat(result.originalName()).isEqualTo("test.txt");
        assertThat(result.size()).isEqualTo(4L);
    }

    @Test
    @DisplayName("議댁옱?섏?_?딅뒗_?뚯씪??硫뷀??곗씠??議고쉶??AdapterDataException??諛쒖깮?쒕떎")
    void selectFileObject_nonExistent_throwsAdapterDataException() {
        // when, then
        assertThatThrownBy(() -> fileService.selectFileObject("nonExistentGuid"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());
    }
}
