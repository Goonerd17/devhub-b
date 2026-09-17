package teamdevhub.devhub.auth.core.auth.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.application.service.AuthResult;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthResultTest {

    @Test
    @DisplayName("LoginResponseDto_瑜??앹꽦?섍퀬_AuthorizationHeader_瑜??뺤씤?????덈떎")
    void canGetAuthorizationHeaderAfterCreatingDto() {
        // given
        String accessToken = "access-token-random";
        String refreshToken = "refresh-token-random";

        // when
        AuthResult authResult = AuthResult.of(accessToken, refreshToken);

        // then
        assertThat(authResult.toAuthorizationHeader()).isEqualTo("Bearer access-token-random");
        assertThat(authResult.accessToken()).isEqualTo(accessToken);
        assertThat(authResult.refreshToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("of_濡??앹꽦??AuthResult_??hasRefreshToken_??true_??")
    void hasRefreshToken_whenCreatedWithRefreshToken_returnsTrue() {
        // given
        AuthResult authResult = AuthResult.of("access-token", "refresh-token");

        // when, then
        assertThat(authResult.hasRefreshToken()).isTrue();
    }

    @Test
    @DisplayName("refreshToken_???놁쑝硫?hasRefreshToken_??false_??")
    void hasRefreshToken_whenNoRefreshToken_returnsFalse() {
        // given
        AuthResult authResult = AuthResult.of("access-token", null);

        // when, then
        assertThat(authResult.hasRefreshToken()).isFalse();
    }
}
