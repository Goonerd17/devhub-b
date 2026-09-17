package teamdevhub.devhub.auth.core.auth.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.application.service.UserCredentialService;
import teamdevhub.devhub.shared.core.common.exception.DomainRuleException;
import teamdevhub.devhub.auth.api.credential.EmailCredentialRegistrationCommand;
import teamdevhub.devhub.auth.api.credential.UpdatePasswordCommand;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeFullUserCredentialRepository;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeRefreshTokenRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeAuthenticatedUserResolver;
import teamdevhub.devhub.fake.pure.application.provider.FakeEncodedPasswordProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserCredentialPasswordServiceTest {

    private UserCredentialService userCredentialService;
    private FakeFullUserCredentialRepository credentialRepository;
    private FakeEncodedPasswordProvider encodedPasswordProvider;

    @BeforeEach
    void init() {
        encodedPasswordProvider = new FakeEncodedPasswordProvider();
        credentialRepository = new FakeFullUserCredentialRepository();

        userCredentialService = new UserCredentialService(
                new FakeTokenParseProvider(),
                new FakeUuidIdentifierProvider(TEST_USER_GUID_1),
                encodedPasswordProvider,
                new FakeAuthenticatedUserResolver(),
                credentialRepository,
                new FakeRefreshTokenRepository()
        );

        // ?대찓???뚯썝媛?낆쑝濡??먭꺽利앸챸 ???
        EmailCredentialRegistrationCommand signupCommand = new EmailCredentialRegistrationCommand(
                TEST_EMAIL_1, TEST_PASSWORD_1);
        userCredentialService.signupEmailUser(signupCommand);
    }

    @Test
    @DisplayName("愿由ъ옄媛_鍮꾨?踰덊샇瑜?珥덇린?뷀븯硫??덈줈??鍮꾨?踰덊샇濡?蹂寃쎈맂??")
    void resetUserPassword_updatesPasswordToNewValue() {
        // when
        userCredentialService.resetUserPassword(TEST_USER_GUID_1, TEST_NEW_PASSWORD);

        // then ????鍮꾨?踰덊샇濡??몄퐫?⑸맂 媛믪씠 ??λ릺?댁빞 ?쒕떎
        String storedPassword = credentialRepository.findEmailCredentialByUserGuid(TEST_USER_GUID_1).getPassword();
        assertThat(storedPassword).isEqualTo(encodedPasswordProvider.encode(TEST_NEW_PASSWORD));
    }

    @Test
    @DisplayName("?꾩옱_鍮꾨?踰덊샇媛_留욎쑝硫?updatePassword媛_?깃났?쒕떎")
    void updatePassword_correctCurrentPassword_succeeds() {
        // given ??currentPassword??raw 鍮꾨?踰덊샇 (?쒕퉬?ㅺ? ?대??먯꽌 matches瑜??ъ슜??鍮꾧탳)
        UpdatePasswordCommand command = UpdatePasswordCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .currentPassword(TEST_PASSWORD_1)
                .newPassword(TEST_NEW_PASSWORD)
                .build();

        // when
        userCredentialService.updatePassword(command);

        // then
        String storedPassword = credentialRepository.findEmailCredentialByUserGuid(TEST_USER_GUID_1).getPassword();
        assertThat(storedPassword).isEqualTo(encodedPasswordProvider.encode(TEST_NEW_PASSWORD));
    }

    @Test
    @DisplayName("?꾩옱_鍮꾨?踰덊샇媛_?由щ㈃_updatePassword媛_?덉쇅瑜?諛쒖깮?쒗궓??")
    void updatePassword_wrongCurrentPassword_throwsDomainRuleException() {
        // given
        UpdatePasswordCommand command = UpdatePasswordCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .currentPassword("wrongPassword")
                .newPassword(TEST_NEW_PASSWORD)
                .build();

        // when, then
        assertThatThrownBy(() -> userCredentialService.updatePassword(command))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.USER_PASSWORD_FAIL.getMessage());
    }
}
