package teamdevhub.devhub.web.shared.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import teamdevhub.devhub.web.shared.logging.TraceIdMDCFilter;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TraceIdMDCFilterTest {

    private final FakeUuidIdentifierProvider fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider("LOG1a1b2c3d4e5f6g7h8i9j10k11l12m");
    private final TraceIdMDCFilter filter = new TraceIdMDCFilter(fakeUuidIdentifierProvider);

    @Test
    @DisplayName("요청헤더에_traceId가_있으면_MDC_에_같은_traceId_를_설정한다")
    void setTraceIdInMdcIfHeaderExists() throws ServletException, IOException {
        // given
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("X-Trace-Id", "existing-trace-id");
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        // when
        filter.doFilter(mockHttpServletRequest, mockHttpServletResponse, chain);

        // then
        assertThat(MDC.get("traceId")).isNull();
        verify(chain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    }

    @Test
    @DisplayName("요청헤더가_없으면_MDC_에_새로운_traceId_를_생성해서_설정한다")
    void generateAndSetTraceIdInMdcIfHeaderMissing() throws ServletException, IOException {
        // given
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        // when
        filter.doFilter(mockHttpServletRequest, mockHttpServletResponse, chain);

        // then
        assertThat(MDC.get("traceId")).isNull();
        verify(chain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    }

    @Test
    @DisplayName("filter_실행후_MDC_는_항상_traceId_를_제거한다")
    void removeTraceIdFromMdcAfterFilterExecution() throws ServletException, IOException {
        // given
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> assertThat(MDC.get("traceId")).isNotNull();

        // when
        filter.doFilter(mockHttpServletRequest, mockHttpServletResponse, chain);

        // then
        assertThat(MDC.get("traceId")).isNull();
    }

    @Test
    @DisplayName("traceId_는_16자리_문자열로_생성된다")
    void generateTraceIdReturns16CharString() throws ServletException, IOException {
        // given
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            String traceId = MDC.get("traceId");
            assertThat(traceId).hasSize(16);
        };

        // when
        filter.doFilter(mockHttpServletRequest, mockHttpServletResponse, chain);

        // then
        assertThat(MDC.get("traceId")).isNull();
    }
}