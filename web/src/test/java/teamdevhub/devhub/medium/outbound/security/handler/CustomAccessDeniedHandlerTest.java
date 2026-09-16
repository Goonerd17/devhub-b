package teamdevhub.devhub.web.outbound.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import teamdevhub.devhub.web.outbound.security.handler.CustomAccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomAccessDeniedHandlerTest {

    @InjectMocks
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private PrintWriter printWriter;

    @Test
    @DisplayName("권한에_따른_접근_거부_예외가_발생하면_JSON_응답으로_전달한다")
    void handleAccessDeniedExceptionWithJsonResponse() throws IOException {
        // given
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        // when
        customAccessDeniedHandler.handle(httpServletRequest, httpServletResponse, exception);

        // then
        verify(httpServletResponse).setStatus(HttpStatus.FORBIDDEN.value());
        verify(httpServletResponse).setContentType("application/json;charset=UTF-8");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(printWriter).write(captor.capture());
        assertThat(captor.getValue()).contains("Access denied");
    }
}
