package teamdevhub.devhub.administration.core.terms.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.administration.core.terms.application.TermsService;
import teamdevhub.devhub.administration.core.terms.domain.Terms;
import teamdevhub.devhub.administration.api.terms.TermsAgreementItem;
import teamdevhub.devhub.administration.api.terms.AgreeTermsCommand;
import teamdevhub.devhub.administration.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.fake.pure.application.port.out.terms.FakeTermsAgreementRepository;
import teamdevhub.devhub.fake.pure.application.port.out.terms.FakeTermsRepository;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.administration.constant.TermsTestConstant.*;

class TermsServiceTest {

    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";

    private TermsService termsService;

    private FakeTermsRepository termsRepository;
    private FakeTermsAgreementRepository termsAgreementRepository;

    @BeforeEach
    void setUp() {
        termsRepository = new FakeTermsRepository();
        termsAgreementRepository = new FakeTermsAgreementRepository();
        IdentifierProvider identifierProvider = () -> UUID.randomUUID().toString();

        termsService = new TermsService(termsRepository, termsAgreementRepository, identifierProvider);
    }

    @Test
    @DisplayName("?쎄????깅줉?섎㈃_?뺤긽?곸쑝濡???λ릺怨?議고쉶?쒕떎")
    void registerTerms_savesTermsSuccessfully() {
        // given
        CreateTermsCommand command = CreateTermsCommand.builder()
                .title(TERMS_TITLE_1)
                .content(TERMS_CONTENT_1)
                .isRequired(REQUIRED)
                .isUsed(USED)
                .build();

        // when
        termsService.registerTerms(command);

        // then
        List<Terms> result = termsService.listTerms();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo(TERMS_TITLE_1);
    }

    @Test
    @DisplayName("?쎄????놁쑝硫?鍮?紐⑸줉??諛섑솚?쒕떎")
    void listTerms_whenEmpty_returnsEmptyList() {
        // when
        List<Terms> result = termsService.listTerms();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("?щ윭_?쎄????깅줉?섎㈃_紐⑤몢_議고쉶?쒕떎")
    void listTerms_whenMultipleTermsRegistered_returnsAll() {
        // given
        termsService.registerTerms(CreateTermsCommand.builder()
                .title(TERMS_TITLE_1).content(TERMS_CONTENT_1).isRequired(REQUIRED).isUsed(USED).build());
        termsService.registerTerms(CreateTermsCommand.builder()
                .title(TERMS_TITLE_2).content(TERMS_CONTENT_2).isRequired(OPTIONAL).isUsed(USED).build());

        // when
        List<Terms> result = termsService.listTerms();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("?좏슚???쎄????숈쓽?섎㈃_?숈쓽_?댁뿭????λ맂??")
    void saveTermsAgreement_validTerms_agreementSaved() {
        // given
        Terms terms = Terms.of(TERMS_GUID_1, TERMS_TITLE_1, TERMS_CONTENT_1, REQUIRED, USED, NOT_DELETED);
        termsRepository.saveTerms(terms);

        AgreeTermsCommand command = AgreeTermsCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .termsAgreementItemList(List.of(new TermsAgreementItem(TERMS_GUID_1, AGREED)))
                .build();

        // when
        termsService.saveTermsAgreement(command);

        // then
        assertThat(termsAgreementRepository.findAll()).hasSize(1);
        assertThat(termsAgreementRepository.findAll().get(0).getTermsGuid()).isEqualTo(TERMS_GUID_1);
        assertThat(termsAgreementRepository.findAll().get(0).isAgreed()).isTrue();
    }

    @Test
    @DisplayName("?숈쓽_紐⑸줉???녿뒗_?쎄?_GUID媛_?ы븿?섎㈃_?덉쇅媛_諛쒖깮?쒕떎")
    void saveTermsAgreement_termsNotFound_throwsBusinessRuleException() {
        // given ??repository is empty, so the guid won't be found
        AgreeTermsCommand command = AgreeTermsCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .termsAgreementItemList(List.of(new TermsAgreementItem(TERMS_GUID_1, AGREED)))
                .build();

        // when, then
        assertThatThrownBy(() -> termsService.saveTermsAgreement(command))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.UNKNOWN_FAIL.getMessage());
    }

    @Test
    @DisplayName("??젣???쎄????숈쓽?섎㈃_?꾨찓???덉쇅媛_諛쒖깮?쒕떎")
    void saveTermsAgreement_deletedTerms_throwsDomainRuleException() {
        // given
        Terms deletedTerms = Terms.of(TERMS_GUID_1, TERMS_TITLE_1, TERMS_CONTENT_1, REQUIRED, USED, DELETED);
        termsRepository.saveTerms(deletedTerms);

        AgreeTermsCommand command = AgreeTermsCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .termsAgreementItemList(List.of(new TermsAgreementItem(TERMS_GUID_1, AGREED)))
                .build();

        // when, then
        assertThatThrownBy(() -> termsService.saveTermsAgreement(command))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.INVALID_TERMS.getMessage());
    }

    @Test
    @DisplayName("?꾩닔_?쎄????숈쓽?섏?_?딆쑝硫??꾨찓???덉쇅媛_諛쒖깮?쒕떎")
    void saveTermsAgreement_requiredTermsNotAgreed_throwsDomainRuleException() {
        // given
        Terms requiredTerms = Terms.of(TERMS_GUID_1, TERMS_TITLE_1, TERMS_CONTENT_1, REQUIRED, USED, NOT_DELETED);
        termsRepository.saveTerms(requiredTerms);

        AgreeTermsCommand command = AgreeTermsCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .termsAgreementItemList(List.of(new TermsAgreementItem(TERMS_GUID_1, NOT_AGREED)))
                .build();

        // when, then
        assertThatThrownBy(() -> termsService.saveTermsAgreement(command))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.INVALID_TERMS_AGREEMENT.getMessage());
    }

    @Test
    @DisplayName("?좏깮_?쎄??_?숈쓽?섏?_?딆븘????λ맂??")
    void saveTermsAgreement_optionalTermsNotAgreed_saved() {
        // given
        Terms optionalTerms = Terms.of(TERMS_GUID_1, TERMS_TITLE_1, TERMS_CONTENT_1, OPTIONAL, USED, NOT_DELETED);
        termsRepository.saveTerms(optionalTerms);

        AgreeTermsCommand command = AgreeTermsCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .termsAgreementItemList(List.of(new TermsAgreementItem(TERMS_GUID_1, NOT_AGREED)))
                .build();

        // when
        termsService.saveTermsAgreement(command);

        // then
        assertThat(termsAgreementRepository.findAll()).hasSize(1);
        assertThat(termsAgreementRepository.findAll().get(0).isAgreed()).isFalse();
    }
}
