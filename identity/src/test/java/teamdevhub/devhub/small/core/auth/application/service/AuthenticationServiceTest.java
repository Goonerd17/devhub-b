package teamdevhub.devhub.identity.core.auth.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.core.auth.application.service.AuthResult;
import teamdevhub.devhub.identity.core.auth.application.service.AuthenticationService;
import teamdevhub.devhub.identity.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.platform.core.common.audit.AuditInfo;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.application.service.CurrentMemberRoleService;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeRefreshTokenRepository;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenIssueProvider;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    private FakeRefreshTokenRepository refreshTokenRepository;
    private FakeUserRepository userRepository;

    @BeforeEach
    void init() {
        FakeTokenIssueProvider fakeTokenIssueProvider = new FakeTokenIssueProvider();
        refreshTokenRepository = new FakeRefreshTokenRepository();
        userRepository = new FakeUserRepository();

        userRepository.givenUser(
                User.of(
                        TEST_USER_GUID_1,
                        UserRole.USER,
                        "tester",
                        null,
                        null,
                        36.5,
                        false,
                        null,
                        false,
                        null,
                        AuditInfo.empty()
                )
        );

        authenticationService = new AuthenticationService(
                fakeTokenIssueProvider,
                refreshTokenRepository,
                new CurrentMemberRoleService(userRepository)
        );
    }

    @Test
    @DisplayName("嚥≪뮄??紐꾩뱽_??롢늺_??り쉭??쎈꽅?怨뚮궢_?귐뗫늄??됰뻻?醫뤾쿃??獄쏆뮄???뺣뼄")
    void issueAccessAndRefreshTokenWhenLogin() {
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(
                        TEST_USER_GUID_1,
                        TEST_EMAIL_1,
                        MemberRole.USER
                );

        AuthResult authResult = authenticationService.login(authenticatedUser);

        assertThat(authResult).isNotNull();
        assertThat(authResult.accessToken())
                .isEqualTo("access-token-" + TEST_USER_GUID_1);
        assertThat(authResult.refreshToken())
                .isEqualTo("refresh-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("嚥≪뮄??紐꾩뱽_??롢늺_?귐뗫늄??됰뻻?醫뤾쿃?????貫留??")
    void storeRefreshTokenWhenLogin() {
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(
                        TEST_USER_GUID_1,
                        TEST_EMAIL_1,
                        MemberRole.USER
                );

        authenticationService.login(authenticatedUser);

        Optional<RefreshToken> refreshToken =
                refreshTokenRepository.findByUserGuid(TEST_USER_GUID_1);

        assertThat(refreshToken)
                .isPresent()
                .get()
                .extracting(RefreshToken::token)
                .isEqualTo("refresh-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("OAuth_嚥≪뮄??紐꾩뱽_??롢늺_??り쉭??쎈꽅?怨뚮궢_?귐뗫늄??됰뻻?醫뤾쿃??獄쏆뮄???뺣뼄")
    void issueAccessAndRefreshTokenWhenLoginWithOAuth() {
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(
                        TEST_USER_GUID_1,
                        TEST_EMAIL_1,
                        MemberRole.USER
                );

        AuthResult authResult = authenticationService.login(authenticatedUser);

        assertThat(authResult).isNotNull();
        assertThat(authResult.accessToken())
                .isEqualTo("access-token-" + TEST_USER_GUID_1);
        assertThat(authResult.refreshToken())
                .isEqualTo("refresh-token-" + TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("??り쉭??쎈꽅????而삥묾?????덉쨮???귐뗫늄??됰뻻?醫뤾쿃????ｍ뜞_獄쏆뮄???뺣뼄")
    void reissueAccessToken_alsoRotatesRefreshToken() {
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(
                        TEST_USER_GUID_1,
                        TEST_EMAIL_1,
                        MemberRole.USER
                );

        refreshTokenRepository.save(
                RefreshToken.of(TEST_USER_GUID_1, "old-refresh-token")
        );

        AuthResult authResult =
                authenticationService.reissueAccessToken(authenticatedUser);

        assertThat(authResult.accessToken())
                .isEqualTo("access-token-" + TEST_USER_GUID_1);

        assertThat(authResult.refreshToken())
                .isEqualTo("refresh-token-" + TEST_USER_GUID_1);

        assertThat(refreshTokenRepository.findByUserGuid(TEST_USER_GUID_1))
                .isPresent()
                .hasValueSatisfying(token ->
                        assertThat(token.token())
                                .isEqualTo("refresh-token-" + TEST_USER_GUID_1));
    }

    @Test
    @DisplayName("嚥≪뮄??袁⑹뜍????롢늺_?귐뗫늄??됰뻻?醫뤾쿃???????뺣뼄")
    void deleteRefreshTokenWhenLogout() {
        refreshTokenRepository.save(
                RefreshToken.of(
                        TEST_USER_GUID_1,
                        "refresh-token-" + TEST_USER_GUID_1
                )
        );

        authenticationService.revoke(TEST_USER_GUID_1);

        assertThat(refreshTokenRepository.contains(TEST_USER_GUID_1))
                .isFalse();
    }

    @Test
    @DisplayName("?醫뤾쿃??_?袁⑤뼎獄쏆룇?_亦낅슦釉???袁⑤빒_DB???袁⑹삺亦낅슦釉??곗쨮_獄쏆뮄???뺣뼄")
    void login_usesCurrentRoleFromRepository() {
        userRepository.givenUser(
                User.of(
                        TEST_USER_GUID_1,
                        UserRole.ADMIN,
                        "admin",
                        null,
                        null,
                        36.5,
                        false,
                        null,
                        false,
                        null,
                        AuditInfo.empty()
                )
        );

        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(
                        TEST_USER_GUID_1,
                        TEST_EMAIL_1,
                        MemberRole.USER
                );

        authenticationService.login(authenticatedUser);

        assertThat(userRepository.wasCalled("findByUserGuid"))
                .isTrue();

        assertThat(userRepository.callCount("findByUserGuid"))
                .isEqualTo(1);
    }
}
