package teamdevhub.devhub.medium.outbound.file.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.media.core.application.FileMetadata;
import teamdevhub.devhub.platform.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.media.outbound.adapter.FileMetadataAdapter;
import teamdevhub.devhub.media.outbound.persistence.JpaFileRepository;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@Transactional
class FileMetadataAdapterTest {

    @Autowired
    private FileMetadataAdapter fileMetadataAdapter;

    @Autowired
    private JpaFileRepository jpaFileRepository;

    @BeforeEach
    void init() {
        jpaFileRepository.deleteAll();
    }

    private FileMetadata sampleMetadata(String guid) {
        return FileMetadata.create(guid, "sample.png", "png", "/files/" + guid, 512L);
    }

    @Test
    @DisplayName("?뚯씪_硫뷀??곗씠?곕?_??ν븯硫?DB????λ맂??")
    void save_persistsFileMetadata() {
        // given
        FileMetadata metadata = sampleMetadata("FILE_GUID_001");

        // when
        FileMetadata saved = fileMetadataAdapter.save(metadata);

        // then
        assertThat(saved.fileGuid()).isEqualTo("FILE_GUID_001");
        assertThat(saved.originalName()).isEqualTo("sample.png");
        assertThat(jpaFileRepository.findByFileGuid("FILE_GUID_001")).isPresent();
    }

    @Test
    @DisplayName("?뚯씪_GUID濡?硫뷀??곗씠?곕?_議고쉶?섎㈃_??λ맂_?뺣낫瑜?諛섑솚?쒕떎")
    void find_existingGuid_returnsFileMetadata() {
        // given
        fileMetadataAdapter.save(sampleMetadata("FILE_GUID_002"));

        // when
        FileMetadata result = fileMetadataAdapter.find("FILE_GUID_002");

        // then
        assertThat(result.fileGuid()).isEqualTo("FILE_GUID_002");
        assertThat(result.originalName()).isEqualTo("sample.png");
        assertThat(result.extensionName()).isEqualTo("png");
        assertThat(result.size()).isEqualTo(512L);
    }

    @Test
    @DisplayName("議댁옱?섏?_?딅뒗_GUID濡?議고쉶?섎㈃_AdapterDataException??諛쒖깮?쒕떎")
    void find_nonExistentGuid_throwsAdapterDataException() {
        // when, then
        assertThatThrownBy(() -> fileMetadataAdapter.find("NOT_EXIST_GUID"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());
    }

    @Test
    @DisplayName("?뚯씪_GUID濡???젣?섎㈃_DB?먯꽌_?쒓굅?쒕떎")
    void deleteByFileGuid_removesFromDb() {
        // given
        fileMetadataAdapter.save(sampleMetadata("FILE_GUID_003"));
        assertThat(jpaFileRepository.findByFileGuid("FILE_GUID_003")).isPresent();

        // when
        fileMetadataAdapter.deleteByFileGuid("FILE_GUID_003");

        // then
        assertThat(jpaFileRepository.findByFileGuid("FILE_GUID_003")).isEmpty();
    }

    @Test
    @DisplayName("??λ맂_?뚯씪??path_?뺣낫媛_?щ컮瑜닿쾶_?좎??쒕떎")
    void save_pathIsPreserved() {
        // given
        FileMetadata metadata = FileMetadata.create("FILE_GUID_004", "doc.pdf", "pdf", "/uploads/docs/FILE_GUID_004", 2048L);

        // when
        fileMetadataAdapter.save(metadata);
        FileMetadata found = fileMetadataAdapter.find("FILE_GUID_004");

        // then
        assertThat(found.path()).isEqualTo("/uploads/docs/FILE_GUID_004");
    }
}
