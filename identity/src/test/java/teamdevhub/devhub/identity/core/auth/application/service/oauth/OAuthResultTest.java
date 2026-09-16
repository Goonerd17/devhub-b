package teamdevhub.devhub.identity.core.auth.application.service.oauth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.core.auth.application.service.AuthResult;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthResult;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.SignupStatus;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OAuthResultTest {

    private static final String ACCESS_TOKEN = "access-token-1";
    private static final String REFRESH_TOKEN = "refresh-token-1";
    private static final String TEMP_TOKEN = "temp-token-1";

    @Test
    @DisplayName("loggedIn_팩토리_메서드로_로그인_완료_OAuthAuthResponseDto_를_생성할_수_있다")
    void canCreateLoggedInDto() {
        // given
        AuthResult authResult = AuthResult.of(ACCESS_TOKEN, REFRESH_TOKEN);

        // when
        OAuthResult oauthAuthResult = OAuthResult.loggedIn(authResult);

        // then
        assertThat(oauthAuthResult.signupStatus()).isEqualTo(SignupStatus.COMPLETED);
        assertThat(oauthAuthResult.accessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(oauthAuthResult.refreshToken()).isEqualTo(REFRESH_TOKEN);
        assertThat(oauthAuthResult.tempToken()).isNull();
    }

    @Test
    @DisplayName("requiresSignup_팩토리_메서드로_회원가입_필요_OAuthAuthResponseDto_를_생성할_수_있다")
    void canCreateRequiresSignupDto() {
        // when
        OAuthResult oauthAuthResult = OAuthResult.requiresSignup(TEMP_TOKEN);

        // then
        assertThat(oauthAuthResult.signupStatus()).isEqualTo(SignupStatus.PENDING);
        assertThat(oauthAuthResult.tempToken()).isEqualTo(TEMP_TOKEN);
        assertThat(oauthAuthResult.accessToken()).isNull();
        assertThat(oauthAuthResult.refreshToken()).isNull();
    }

    @Test
    @DisplayName("toauthorizationHeader_로_Bearer_토큰_헤더를_생성할_수_있다")
    void canGetAuthorizationHeader() {
        // given
        OAuthResult oauthAuthResult = OAuthResult.builder()
                .accessToken(ACCESS_TOKEN)
                .build();

        // when
        String header = oauthAuthResult.toauthorizationHeader();

        // then
        assertThat(header).isEqualTo("Bearer access-token-1");
    }

    @Test
    @DisplayName("accessToken_이_없는_PENDING_상태에서_toauthorizationHeader_호출_시_예외가_발생한다")
    void toAuthorizationHeader_whenAccessTokenIsNull_throwsException() {
        // given
        OAuthResult pendingResult = OAuthResult.requiresSignup(TEMP_TOKEN);

        // when, then
        assertThatThrownBy(pendingResult::toauthorizationHeader)
                .isInstanceOf(BusinessRuleException.class);
    }
}
