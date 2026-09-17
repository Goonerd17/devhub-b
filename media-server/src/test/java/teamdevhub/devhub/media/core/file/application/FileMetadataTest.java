package teamdevhub.devhub.media.core.file.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.media.core.application.FileMetadata;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileMetadataTest {

    @Test
    @DisplayName("유효한_값으로_FileMetadata를_생성하면_모든_필드가_설정된다")
    void create_validArgs_returnsFileMetadata() {
        // when
        FileMetadata metadata = FileMetadata.create("guid1", "test.png", "png", "/files/guid1", 1024L);

        // then
        assertThat(metadata.fileGuid()).isEqualTo("guid1");
        assertThat(metadata.originalName()).isEqualTo("test.png");
        assertThat(metadata.extensionName()).isEqualTo("png");
        assertThat(metadata.path()).isEqualTo("/files/guid1");
        assertThat(metadata.size()).isEqualTo(1024L);
    }

    @Test
    @DisplayName("파일명이_null이면_BusinessRuleException이_발생한다")
    void create_nullOriginalName_throwsBusinessRuleException() {
        // when, then
        assertThatThrownBy(() -> FileMetadata.create("guid1", null, "png", "/files/guid1", 1024L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.FILE_NAME_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("파일명이_빈값이면_BusinessRuleException이_발생한다")
    void create_blankOriginalName_throwsBusinessRuleException() {
        // when, then
        assertThatThrownBy(() -> FileMetadata.create("guid1", "  ", "png", "/files/guid1", 1024L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.FILE_NAME_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("확장자가_null이면_BusinessRuleException이_발생한다")
    void create_nullExtensionName_throwsBusinessRuleException() {
        // when, then
        assertThatThrownBy(() -> FileMetadata.create("guid1", "test.png", null, "/files/guid1", 1024L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.FILE_EXTENSION_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("확장자가_빈값이면_BusinessRuleException이_발생한다")
    void create_blankExtensionName_throwsBusinessRuleException() {
        // when, then
        assertThatThrownBy(() -> FileMetadata.create("guid1", "test.png", "", "/files/guid1", 1024L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.FILE_EXTENSION_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("파일_크기가_0이면_BusinessRuleException이_발생한다")
    void create_zeroSize_throwsBusinessRuleException() {
        // when, then
        assertThatThrownBy(() -> FileMetadata.create("guid1", "test.png", "png", "/files/guid1", 0L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.FILE_SIZE_INVALID.getMessage());
    }

    @Test
    @DisplayName("파일_크기가_음수이면_BusinessRuleException이_발생한다")
    void create_negativeSize_throwsBusinessRuleException() {
        // when, then
        assertThatThrownBy(() -> FileMetadata.create("guid1", "test.png", "png", "/files/guid1", -1L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.FILE_SIZE_INVALID.getMessage());
    }
}
