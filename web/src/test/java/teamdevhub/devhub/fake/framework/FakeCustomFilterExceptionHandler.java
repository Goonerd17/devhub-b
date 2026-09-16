package teamdevhub.devhub.fake.framework;

import jakarta.servlet.http.HttpServletResponse;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.web.outbound.security.filter.CustomFilterExceptionHandler;

public class FakeCustomFilterExceptionHandler extends CustomFilterExceptionHandler {

    private boolean handled = false;

    @Override
    public void handle(HttpServletResponse response, ErrorCode errorCode) {
        this.handled = true;
    }

    public boolean isHandled() {
        return handled;
    }
}