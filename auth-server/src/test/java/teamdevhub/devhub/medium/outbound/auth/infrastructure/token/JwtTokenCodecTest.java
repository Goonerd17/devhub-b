package teamdevhub.devhub.auth.outbound.auth.infrastructure.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.auth.outbound.auth.infrastructure.token.JwtTokenCodec;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.shared.outbound.common.exception.AuthRuleException;
import teamdevhub.devhub.auth.core.auth.port.out.token.vo.AccessTokenInfo;
import teamdevhub.devhub.auth.core.auth.port.out.token.vo.TempTokenInfo;
import teamdevhub.devhub.fake.pure.application.provider.FakeTimeProvider;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;
import static teamdevhub.devhub.shared.shared.enums.ErrorCode.TOKEN_EXPIRED;
import static teamdevhub.devhub.shared.shared.enums.ErrorCode.TOKEN_INVALID;
import static teamdevhub.devhub.member.api.MemberRole.USER;

class JwtTokenCodecTest {

    private JwtTokenCodec jwtTokenCodec;

    @BeforeEach
    void init() throws Exception {
        FakeTimeProvider fakeTimeProvider = new FakeTimeProvider(LocalDateTime.now());
        jwtTokenCodec = new JwtTokenCodec(fakeTimeProvider);

        String secret = "abcdefghijklmnopqrstuvwxyz123456";
        String base64Key = Base64.getEncoder().encodeToString(secret.getBytes());

        Field secretKeyField = JwtTokenCodec.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtTokenCodec, base64Key);

        jwtTokenCodec.init();
    }

    @Test
    @DisplayName("accessToken_?앹꽦_???좏겙_?뺣낫瑜??뺤긽?곸쑝濡?異붿텧?쒕떎")
    void createAccessTokenAndExtractInfo() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);

        // when
        String accessToken = jwtTokenCodec.createAccessToken(authenticatedUser);
        AccessTokenInfo accessTokenInfo = jwtTokenCodec.getAccessTokenInfo(accessToken);

        // then
        assertThat(accessTokenInfo.userGuid()).isEqualTo(authenticatedUser.userGuid());
        assertThat(accessTokenInfo.email()).isEqualTo(TEST_EMAIL_1);
        assertThat(accessTokenInfo.userRole()).isEqualTo(USER);
    }

    @Test
    @DisplayName("refreshToken_??accessToken_parser_???ｌ쑝硫?TOKEN_INVALID_?덉쇅媛_諛쒖깮?쒕떎")
    void extractAccessTokenInfoWithRefreshTokenThrows() {
        // given
        String refreshToken = jwtTokenCodec.createRefreshToken(TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(() ->
                jwtTokenCodec.getAccessTokenInfo(refreshToken))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("refreshToken_?앹꽦_??userGuid_瑜??뺤긽_異붿텧?????덈떎")
    void createRefreshTokenAndExtractUserGuid() {
        // given
        String userGuid = TEST_USER_GUID_1;

        // when
        String refreshToken = jwtTokenCodec.createRefreshToken(userGuid);
        String extracted = jwtTokenCodec.getRefreshTokenInfo(refreshToken);

        // then
        assertThat(extracted).isEqualTo(userGuid);
    }

    @Test
    @DisplayName("accessToken_??refreshToken_parser_???ｌ쑝硫?TOKEN_INVALID_?덉쇅媛_諛쒖깮?쒕떎")
    void extractRefreshTokenInfoWithAccessTokenThrows() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        String accessToken = jwtTokenCodec.createAccessToken(authenticatedUser);

        // when, then
        assertThatThrownBy(() ->
                jwtTokenCodec.getRefreshTokenInfo(accessToken))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("tempToken_?앹꽦_???좏겙_?뺣낫媛_?뺤긽_異붿텧?쒕떎")
    void createTempTokenAndExtractInfo() {
        // given
        String oauthId = "oauth-id-123";

        // when
        String tempToken = jwtTokenCodec.createTempToken(
                oauthId,
                VerificationProvider.GOOGLE,
                TEST_EMAIL_1
        );
        TempTokenInfo tempTokenInfo = jwtTokenCodec.getTempTokenInfo(tempToken);

        // then
        assertThat(tempTokenInfo.oauthId()).isEqualTo(oauthId);
        assertThat(tempTokenInfo.verificationProvider()).isEqualTo(VerificationProvider.GOOGLE);
        assertThat(tempTokenInfo.email()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("accessToken_??tempToken_parser_???ｌ쑝硫?TOKEN_INVALID_?덉쇅媛_諛쒖깮?쒕떎")
    void extractTempTokenInfoWithAccessTokenThrows() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        String accessToken = jwtTokenCodec.createAccessToken(authenticatedUser);


        // when, then
        assertThatThrownBy(() ->
                jwtTokenCodec.getTempTokenInfo(accessToken))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("Bearer_prefix_瑜??뺤긽?곸쑝濡??쒓굅?쒕떎")
    void removeBearer_success() {
        // given
        String tokenWithBearer = "Bearer abc.def.ghi";

        // when
        String result = jwtTokenCodec.removeBearer(tokenWithBearer);

        // then
        assertThat(result).isEqualTo("abc.def.ghi");
    }

    @Test
    @DisplayName("Bearer_prefix_?놁쑝硫?TOKEN_INVALID_?덉쇅媛_諛쒖깮?쒕떎")
    void removeBearer_withoutPrefixThrows() {
        // when, then
        assertThatThrownBy(() ->
                jwtTokenCodec.removeBearer("abc.def.ghi"))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("Bearer_null_?낅젰_??TOKEN_INVALID_?덉쇅媛_諛쒖깮?쒕떎")
    void removeBearer_nullThrows() {
        // when, then
        assertThatThrownBy(() ->
                jwtTokenCodec.removeBearer(null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("留뚮즺??accessToken_?_TOKEN_EXPIRED_?덉쇅媛_諛쒖깮?쒕떎")
    void expiredAccessTokenThrowsAgain() throws NoSuchFieldException, IllegalAccessException {
        // given
        FakeTimeProvider fakeTimeProvider = new FakeTimeProvider(LocalDateTime.now().minusHours(2));
        jwtTokenCodec = new JwtTokenCodec(fakeTimeProvider);
        String secret = "abcdefghijklmnopqrstuvwxyz123456";
        String base64Key = Base64.getEncoder().encodeToString(secret.getBytes());

        Field secretKeyField = JwtTokenCodec.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtTokenCodec, base64Key);

        jwtTokenCodec.init();

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        String accessToken = jwtTokenCodec.createAccessToken(authenticatedUser);
        fakeTimeProvider.setNow(LocalDateTime.now());

        // when, then
        assertThatThrownBy(() ->
                jwtTokenCodec.getAccessTokenInfo(accessToken))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_EXPIRED.getMessage());
    }
}
