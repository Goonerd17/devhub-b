package teamdevhub.devhub.web.outbound.security.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.web.outbound.security.handler.CustomAuthenticationEntryPoint;

import static org.assertj.core.api.Assertions.assertThat;

class CustomAuthenticationEntryPointTest {

    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @BeforeEach
    void init() {
        customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint();
    }

    @Test
    @DisplayName("인증에_실패하면_401_응답과_JSON_메시지를_작성한다")
    void handleAuthenticationFailureWith401AndJson() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException exception = new AuthenticationException("Unauthorized") {};

        // when
        customAuthenticationEntryPoint.commence(request, response, exception);

        // then
        String json = response.getContentAsString();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(json).contains("\"success\":false");
        assertThat(json).contains("\"code\":\"" + ErrorCode.AUTH_INVALID.getCode() + "\"");
        assertThat(json).contains("\"data\":null");
    }
}
