package teamdevhub.devhub.web.outbound.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.web.outbound.security.filter.CustomFilterExceptionHandler;

import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class CustomFilterExceptionHandlerTest {

    private CustomFilterExceptionHandler customFilterExceptionHandler;

    private MockHttpServletResponse mockHttpServletResponse;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        mockHttpServletResponse = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();

        customFilterExceptionHandler = new CustomFilterExceptionHandler();
    }

    @Test
    @DisplayName("httpServletResponse_???먮윭肄붾뱶_?묐떟??JSON_?쇰줈_?묒꽦?쒕떎")
    void writeErrorResponseAsJson() throws Exception {
        // given
        ErrorCode errorCode = ErrorCode.TOKEN_UNSUPPORTED;

        // when
        customFilterExceptionHandler.handle(mockHttpServletResponse, errorCode);

        // then
        assertThat(mockHttpServletResponse.getStatus()).isEqualTo(errorCode.getStatus().value());
        assertThat(mockHttpServletResponse.getContentType()).isEqualTo("application/json;charset=UTF-8");
        assertThat(mockHttpServletResponse.getCharacterEncoding()).isEqualTo("UTF-8");
        String body = mockHttpServletResponse.getContentAsString();
        DataApiResponseDto<?> result = objectMapper.readValue(body, DataApiResponseDto.class);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getError().getMessage()).isEqualTo(errorCode.getMessage());
    }

    @Test
    @DisplayName("?묐떟_?묒꽦_以??덉쇅媛_諛쒖깮?대룄_?몃?濡??꾪뙆?섏?_?딅뒗??")
    void swallowExceptionIfResponseWriteFails(){
        // given
        ErrorCode errorCode = ErrorCode.TOKEN_INVALID;

        HttpServletResponse brokenResponse = new HttpServletResponseWrapper(mockHttpServletResponse) {
            @Override
            public PrintWriter getWriter() {
                throw new RuntimeException("Test Error occurred");
            }
        };

        // when, then
        assertThatCode(
                () -> customFilterExceptionHandler.handle(brokenResponse, errorCode))
                .doesNotThrowAnyException();
    }
}
