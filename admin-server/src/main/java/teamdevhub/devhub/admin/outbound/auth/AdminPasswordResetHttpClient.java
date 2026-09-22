package teamdevhub.devhub.admin.outbound.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.security.AdminPasswordReset;

@Component
public class AdminPasswordResetHttpClient implements AdminPasswordReset {
    private final RestClient client;

    public AdminPasswordResetHttpClient(RestClient.Builder builder,
            @Value("${services.auth.base-url:http://auth-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @Override
    public void reset(String memberGuid, String newPassword) {
        client.put().uri("/internal/member-credentials/{memberGuid}/password", memberGuid)
                .body(new PasswordResetRequest(newPassword)).retrieve().toBodilessEntity();
    }

    private record PasswordResetRequest(String password) {
    }
}
