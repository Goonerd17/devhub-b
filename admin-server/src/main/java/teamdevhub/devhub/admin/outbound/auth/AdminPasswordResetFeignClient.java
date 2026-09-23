package teamdevhub.devhub.admin.outbound.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;

@FeignClient(name = "auth-server", configuration = InternalFeignConfiguration.class)
public interface AdminPasswordResetFeignClient {
    @PutMapping("/internal/member-credentials/{memberGuid}/password")
    void reset(@PathVariable String memberGuid, @RequestBody PasswordResetRequest request);
    record PasswordResetRequest(String password) { }
}
