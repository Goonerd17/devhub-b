package teamdevhub.devhub.admin.outbound.auth;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.shared.security.AdminPasswordReset;

@Component
public class AdminPasswordResetHttpClient implements AdminPasswordReset {
    private final AdminPasswordResetFeignClient client;

    public AdminPasswordResetHttpClient(AdminPasswordResetFeignClient client) {
        this.client = client;
    }

    @Override
    public void reset(String memberGuid, String newPassword) {
        client.reset(memberGuid, new AdminPasswordResetFeignClient.PasswordResetRequest(newPassword));
    }
}
