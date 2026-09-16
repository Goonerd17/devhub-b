package teamdevhub.devhub.web.outbound.security.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.identity.core.auth.port.out.token.vo.AccessTokenInfo;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.fake.framework.FakeCustomFilterExceptionHandler;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenParseProvider;
import teamdevhub.devhub.web.outbound.security.filter.JwtAuthorizationFilter;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.platform.outbound.common.exception.AuthRuleException;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAuthorizationFilterTest {

    private static final String TEST_EMAIL_1 = "user1@example.com";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";

    private JwtAuthorizationFilter jwtAuthorizationFilter;

    private FakeTokenParseProvider tokenParseProvider;
    private FakeCustomFilterExceptionHandler customFilterExceptionHandler;

    private MockHttpServletRequest httpServletRequest;
    private MockHttpServletResponse httpServletResponse;
    private MockFilterChain filterChain;

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        customFilterExceptionHandler = new FakeCustomFilterExceptionHandler();
        jwtAuthorizationFilter = new JwtAuthorizationFilter(tokenParseProvider, customFilterExceptionHandler);

        httpServletRequest = new MockHttpServletRequest();
        httpServletResponse = new MockHttpServletResponse();
        filterChain = new MockFilterChain();

        SecurityContextHolder.clearContext();
    }

    private AccessTokenInfo makeAccessTokenInfo() {
        return new AccessTokenInfo(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                MemberRole.USER
        );
    }

    @Test
    @DisplayName("?좏겙???놁쑝硫??ㅼ쓬_?꾪꽣瑜??ㅽ뻾?쒕떎")
    void proceed_to_next_filter_if_no_token() throws Exception {
        // given

        // when
        jwtAuthorizationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(filterChain.getRequest()).isNotNull(); // ?꾪꽣 泥댁씤 ?ㅽ뻾??
    }

    @Test
    @DisplayName("?좏슚?섏?_?딆?_?좏겙?대㈃_CustomFilterExceptionHandler_瑜??몄텧?쒕떎")
    void call_error_handler_if_token_invalid() throws Exception {
        // given
        String token = "Bearer invalid";
        httpServletRequest.addHeader("Authorization", token);
        tokenParseProvider = new FakeTokenParseProvider() {
            @Override
            public AccessTokenInfo getAccessTokenInfo(String accessToken) {
                throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
            }
        };
        jwtAuthorizationFilter = new JwtAuthorizationFilter(tokenParseProvider, customFilterExceptionHandler);

        // when
        jwtAuthorizationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // then
        assertThat(customFilterExceptionHandler.isHandled()).isTrue();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("?좏슚???≪꽭???좏겙?대㈃_Authentication_???ㅼ젙?섍퀬_?ㅼ쓬_?꾪꽣瑜??ㅽ뻾?쒕떎")
    void set_authentication_and_proceed_if_access_token_valid() throws Exception {
        // given
        String token = "Bearer valid";
        httpServletRequest.addHeader("Authorization", token);

        AccessTokenInfo tokenInfo = makeAccessTokenInfo();
        tokenParseProvider.givenAccessToken("valid", tokenInfo);

        // when
        jwtAuthorizationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        assertThat(user.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(user.loginId()).isEqualTo(TEST_EMAIL_1);
        assertThat(authentication.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly(MemberRole.USER.getAuthority());
    }
}
