package teamdevhub.devhub.auth.core.auth.application.service.oauth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.application.service.oauth.vo.OAuthUserResult;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.member.api.MemberRole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OAuthUserResultTest {

    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_EMAIL_1 = "user1@example.com";

    @Test
    @DisplayName("success_?⑺넗由?硫붿꽌?쒕줈_濡쒓렇??媛?ν븳_OAuthUserResult_瑜??앹꽦?????덈떎")
    void success_createsLoginAvailableResult() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);

        // when
        OAuthUserResult result = OAuthUserResult.success(authenticatedUser);

        // then
        assertThat(result.loginAvailable()).isTrue();
        assertThat(result.authenticatedUser()).isEqualTo(authenticatedUser);
    }

    @Test
    @DisplayName("requiresSignup_?⑺넗由?硫붿꽌?쒕줈_濡쒓렇??遺덇?_OAuthUserResult_瑜??앹꽦?????덈떎")
    void requiresSignup_createsLoginUnavailableResult() {
        // when
        OAuthUserResult result = OAuthUserResult.requiresSignup();

        // then
        assertThat(result.loginAvailable()).isFalse();
        assertThat(result.authenticatedUser()).isNull();
    }

    @Test
    @DisplayName("loginAvailable_??true_?대㈃_requireAuthenticatedUser_濡??ъ슜?먮?_媛?몄삱_???덈떎")
    void requireAuthenticatedUser_whenLoginAvailable_returnsUser() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        OAuthUserResult result = OAuthUserResult.success(authenticatedUser);

        // when
        AuthenticatedUser returned = result.requireAuthenticatedUser();

        // then
        assertThat(returned).isEqualTo(authenticatedUser);
    }

    @Test
    @DisplayName("loginAvailable_??false_?대㈃_requireAuthenticatedUser_?몄텧_???덉쇅媛_諛쒖깮?쒕떎")
    void requireAuthenticatedUser_whenLoginUnavailable_throwsException() {
        // given
        OAuthUserResult result = OAuthUserResult.requiresSignup();

        // when, then
        assertThatThrownBy(result::requireAuthenticatedUser)
                .isInstanceOf(BusinessRuleException.class);
    }
}
