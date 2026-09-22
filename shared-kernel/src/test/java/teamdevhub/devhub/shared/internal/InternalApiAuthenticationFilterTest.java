package teamdevhub.devhub.shared.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

class InternalApiAuthenticationFilterTest {
    private final InternalApiAuthenticationFilter filter = new InternalApiAuthenticationFilter("expected-key");

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void rejectsRequestWithoutInternalKey() throws Exception {
        var request = new MockHttpServletRequest("GET", "/internal/test");
        var response = new MockHttpServletResponse();
        var continued = new AtomicBoolean();

        filter.doFilter(request, response, (ignoredRequest, ignoredResponse) -> continued.set(true));

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(continued).isFalse();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void rejectsRequestWithWrongInternalKey() throws Exception {
        var request = new MockHttpServletRequest("GET", "/internal/test");
        request.addHeader(InternalApiAuthenticationFilter.HEADER_NAME, "wrong-key");
        var response = new MockHttpServletResponse();
        var continued = new AtomicBoolean();

        filter.doFilter(request, response, (ignoredRequest, ignoredResponse) -> continued.set(true));

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(continued).isFalse();
    }

    @Test
    void authenticatesRequestWithExpectedInternalKey() throws Exception {
        var request = new MockHttpServletRequest("GET", "/internal/test");
        request.addHeader(InternalApiAuthenticationFilter.HEADER_NAME, "expected-key");
        var response = new MockHttpServletResponse();
        var continued = new AtomicBoolean();

        filter.doFilter(request, response, (ignoredRequest, ignoredResponse) -> continued.set(true));

        assertThat(continued).isTrue();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
                .extracting("authority").containsExactly("ROLE_INTERNAL_SERVICE");
    }
}
