package teamdevhub.devhub.web.outbound.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

@Slf4j
@Component
public class CustomFilterExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handle(HttpServletResponse httpServletResponse, ErrorCode errorCode) {

        try {
            if (httpServletResponse.isCommitted()) {
                httpServletResponse.resetBuffer();
            }

            httpServletResponse.setStatus(errorCode.getStatus().value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");

            DataApiResponseDto<?> result = DataApiResponseDto.failureWithoutData(errorCode);
            String json = objectMapper.writeValueAsString(result);

            httpServletResponse.getWriter().write(json);
            httpServletResponse.getWriter().flush();

        } catch (Exception e) {
            log.error("FilterExceptionHandler response write error", e);
        }
    }
}
