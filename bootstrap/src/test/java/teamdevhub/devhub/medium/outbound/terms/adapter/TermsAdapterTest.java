package teamdevhub.devhub.medium.outbound.terms.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.administration.core.terms.domain.Terms;
import teamdevhub.devhub.administration.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.platform.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.administration.outbound.terms.adapter.TermsAdapter;
import teamdevhub.devhub.administration.outbound.terms.persistence.JpaTermsAgreementRepository;
import teamdevhub.devhub.administration.outbound.terms.persistence.JpaTermsRepository;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.TermsTestConstant.*;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@Transactional
class TermsAdapterTest {

    @Autowired
    private TermsAdapter termsAdapter;

    @Autowired
    private JpaTermsRepository jpaTermsRepository;

    @Autowired
    private JpaTermsAgreementRepository jpaTermsAgreementRepository;

    @BeforeEach
    void init() {
        jpaTermsAgreementRepository.deleteAll();
        jpaTermsRepository.deleteAll();
    }

    private Terms sampleTerms(String guid, String title) {
        return Terms.of(guid, title, TERMS_CONTENT_1, REQUIRED, USED, NOT_DELETED);
    }

    @Test
    @DisplayName("?쎄?????ν븯硫?DB????λ맂??")
    void saveTerms_persistsToDb() {
        // given
        Terms terms = sampleTerms(TERMS_GUID_1, TERMS_TITLE_1);

        // when
        termsAdapter.saveTerms(terms);

        // then
        assertThat(jpaTermsRepository.findByTermsGuid(TERMS_GUID_1)).isPresent();
    }

    @Test
    @DisplayName("?꾩껜_?쎄?_紐⑸줉??議고쉶?섎㈃_??λ맂_紐⑤뱺_?쎄???諛섑솚?쒕떎")
    void listTerms_returnsAllSavedTerms() {
        // given
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_1, TERMS_TITLE_1));
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_2, TERMS_TITLE_2));

        // when
        List<Terms> result = termsAdapter.listTerms();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("?쎄?_GUID濡?議고쉶?섎㈃_?대떦_?쎄???諛섑솚?쒕떎")
    void findByTermsGuid_returnsCorrectTerms() {
        // given
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_1, TERMS_TITLE_1));

        // when
        Terms result = termsAdapter.findByTermsGuid(TERMS_GUID_1);

        // then
        assertThat(result.getTermsGuid()).isEqualTo(TERMS_GUID_1);
        assertThat(result.getTitle()).isEqualTo(TERMS_TITLE_1);
    }

    @Test
    @DisplayName("議댁옱?섏?_?딅뒗_GUID濡?議고쉶?섎㈃_AdapterDataException??諛쒖깮?쒕떎")
    void findByTermsGuid_notFound_throwsAdapterDataException() {
        // when, then
        assertThatThrownBy(() -> termsAdapter.findByTermsGuid("NOT_EXIST_GUID"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.NOT_EXISTED_TERMS_AGREEMENT.getMessage());
    }

    @Test
    @DisplayName("GUID_紐⑸줉?쇰줈_??젣?섏?_?딄퀬_?ъ슜以묒씤_?쎄?留?議고쉶?쒕떎")
    void findAllByTermsGuidIn_returnsOnlyActiveTerms() {
        // given ??GUID_1 is active, GUID_2 is deleted
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_1, TERMS_TITLE_1));
        Terms deletedTerms = Terms.of(TERMS_GUID_2, TERMS_TITLE_2, TERMS_CONTENT_2, OPTIONAL, USED, DELETED);
        termsAdapter.saveTerms(deletedTerms);

        // when
        List<Terms> result = termsAdapter.findAllByTermsGuidIn(Set.of(TERMS_GUID_1, TERMS_GUID_2));

        // then ??only GUID_1 should be returned (not deleted)
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTermsGuid()).isEqualTo(TERMS_GUID_1);
    }

    @Test
    @DisplayName("?쎄?_?숈쓽_紐⑸줉????ν븯硫?DB??紐⑤몢_??λ맂??")
    void saveAll_persistsAllAgreements() {
        // given
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_1, TERMS_TITLE_1));

        TermsAgreement agreement = TermsAgreement.of(AGREEMENT_GUID_1, TERMS_GUID_1, USER_GUID_1, AGREED);

        // when
        termsAdapter.saveAll(List.of(agreement));

        // then
        assertThat(jpaTermsAgreementRepository.findAll()).hasSize(1);
        assertThat(jpaTermsAgreementRepository.findAll().get(0).getTermsGuid()).isEqualTo(TERMS_GUID_1);
        assertThat(jpaTermsAgreementRepository.findAll().get(0).getUserGuid()).isEqualTo(USER_GUID_1);
        assertThat(jpaTermsAgreementRepository.findAll().get(0).isAgreed()).isTrue();
    }

    @Test
    @DisplayName("?щ윭_?쎄?_?숈쓽_??ぉ???쒕쾲????ν븷_???덈떎")
    void saveAll_multipleAgreements_savesAll() {
        // given
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_1, TERMS_TITLE_1));
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_2, TERMS_TITLE_2));

        TermsAgreement agreement1 = TermsAgreement.of(AGREEMENT_GUID_1, TERMS_GUID_1, USER_GUID_1, AGREED);
        TermsAgreement agreement2 = TermsAgreement.of(AGREEMENT_GUID_2, TERMS_GUID_2, USER_GUID_1, NOT_AGREED);

        // when
        termsAdapter.saveAll(List.of(agreement1, agreement2));

        // then
        assertThat(jpaTermsAgreementRepository.findAll()).hasSize(2);
    }
}
