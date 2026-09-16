package teamdevhub.devhub.web.api.web.resolver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.web.api.web.resolver.LoginUser;
import teamdevhub.devhub.web.api.web.resolver.LoginUserArgumentResolver;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.fake.framework.FakeAuthentication;
import teamdevhub.devhub.platform.outbound.common.exception.AuthRuleException;
import teamdevhub.devhub.identity.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoginUserArgumentResolverTest {

    private static final String TEST_EMAIL_1 = "user1@example.com";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";

    private LoginUserArgumentResolver loginUserArgumentResolver;

    static class TestController {

        public void testMethod(@LoginUser AuthenticatedUser authenticatedUser) {
        }

        public void optionalMethod(@LoginUser(required = false) AuthenticatedUser authenticatedUser) {
        }

        public void noAnnotationMethod(AuthenticatedUser authenticatedUser) {
        }

        public void wrongTypeMethod(@LoginUser String user) {
        }
    }

    @BeforeEach
    void init() {
        loginUserArgumentResolver = new LoginUserArgumentResolver();
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    private MethodParameter loginUserParameter() throws NoSuchMethodException {
        Method method = TestController.class.getMethod("testMethod", AuthenticatedUser.class);
        return new MethodParameter(method, 0);
    }

    private MethodParameter optionalLoginUserParameter() throws NoSuchMethodException {
        Method method = TestController.class.getMethod("optionalMethod", AuthenticatedUser.class);
        return new MethodParameter(method, 0);
    }

    @Test
    @DisplayName("supportsParameter_??LoginUser_??????뵠??띾궢_AuthenticatedUser_????놁뱽_筌왖?癒곕립??")
    void supportsParameter_returnsTrueForLoginUserAnnotatedAuthenticatedUser() throws Exception {
        // given
        MethodParameter parameter = loginUserParameter();

        // when
        boolean result = loginUserArgumentResolver.supportsParameter(parameter);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("supportsParameter_????????뵠??륁뵠_??곸몵筌?false_??獄쏆꼹???뺣뼄")
    void supportsParameter_returnsFalseWithoutAnnotation() throws Exception {
        // given
        Method method = TestController.class.getMethod("noAnnotationMethod", AuthenticatedUser.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        // when
        boolean result = loginUserArgumentResolver.supportsParameter(parameter);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("supportsParameter_??????놁뵠_??삘뀮筌?false_??獄쏆꼹???뺣뼄")
    void supportsParameter_returnsFalseForWrongType() throws Exception {
        // given
        Method method = TestController.class.getMethod("wrongTypeMethod", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        // when
        boolean result = loginUserArgumentResolver.supportsParameter(parameter);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("UserAuthentication_principal_????AuthenticatedUser_??獄쏆꼹???뺣뼄")
    void returnAuthenticatedUserIfUserAuthenticationPrincipal() throws Exception {
        // given
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);

        UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

        SecurityContextHolder.getContext()
                .setAuthentication(new FakeAuthentication(userAuthentication));

        // when
        Object resolved =
                loginUserArgumentResolver.resolveArgument(loginUserParameter(), null, null, null);

        // then
        assertThat(resolved).isInstanceOf(AuthenticatedUser.class);
        assertThat(resolved).isEqualTo(authenticatedUser);
    }

    @Test
    @DisplayName("AuthenticatedUser_principal_????域밸챶?嚥?獄쏆꼹???뺣뼄")
    void returnAuthenticatedUserIfPrincipalIsAlreadyAuthenticatedUser() throws Exception {
        // given
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);

        SecurityContextHolder.getContext()
                .setAuthentication(new FakeAuthentication(authenticatedUser));

        // when
        Object resolved =
                loginUserArgumentResolver.resolveArgument(loginUserParameter(), null, null, null);

        // then
        assertThat(resolved).isSameAs(authenticatedUser);
    }

    @Test
    @DisplayName("authentication????곸몵筌???됱뇚揶쎛_獄쏆뮇源??뺣뼄")
    void throwIfAuthenticationMissing() {
        // given
        SecurityContextHolder.clearContext();

        // then
        assertThatThrownBy(() ->
                loginUserArgumentResolver.resolveArgument(loginUserParameter(), null, null, null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("principal??null??????됱뇚揶쎛_獄쏆뮇源??뺣뼄")
    void throwIfPrincipalIsNull() {
        // given
        SecurityContextHolder.getContext()
                .setAuthentication(new FakeAuthentication(null));

        // then
        assertThatThrownBy(() ->
                loginUserArgumentResolver.resolveArgument(loginUserParameter(), null, null, null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("principal_????놁뵠_??삘뀮筌???됱뇚揶쎛_獄쏆뮇源??뺣뼄")
    void throwIfPrincipalTypeMismatch() {
        // given
        SecurityContextHolder.getContext()
                .setAuthentication(new FakeAuthentication("invalid-principal"));

        // then
        assertThatThrownBy(() ->
                loginUserArgumentResolver.resolveArgument(loginUserParameter(), null, null, null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("required_false??욱?authentication????곸몵筌?null??獄쏆꼹???뺣뼄")
    void returnNullIfOptionalAndAuthenticationMissing() throws Exception {
        // given
        SecurityContextHolder.clearContext();

        // when
        Object resolved =
                loginUserArgumentResolver.resolveArgument(optionalLoginUserParameter(), null, null, null);

        // then
        assertThat(resolved).isNull();
    }

    @Test
    @DisplayName("required_false??욱?principal??null????null??獄쏆꼹???뺣뼄")
    void returnNullIfOptionalAndPrincipalNull() throws Exception {
        // given
        SecurityContextHolder.getContext()
                .setAuthentication(new FakeAuthentication(null));

        // when
        Object resolved =
                loginUserArgumentResolver.resolveArgument(optionalLoginUserParameter(), null, null, null);

        // then
        assertThat(resolved).isNull();
    }

    @Test
    @DisplayName("required_false??욱?principal_????놁뵠_??삘뀮筌?null??獄쏆꼹???뺣뼄")
    void returnNullIfOptionalAndPrincipalTypeMismatch() throws Exception {
        // given
        SecurityContextHolder.getContext()
                .setAuthentication(new FakeAuthentication("invalid-principal"));

        // when
        Object resolved =
                loginUserArgumentResolver.resolveArgument(optionalLoginUserParameter(), null, null, null);

        // then
        assertThat(resolved).isNull();
    }
}
