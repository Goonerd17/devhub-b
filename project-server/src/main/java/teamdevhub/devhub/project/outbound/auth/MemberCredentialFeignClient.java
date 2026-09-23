package teamdevhub.devhub.project.outbound.auth;

import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;
import teamdevhub.devhub.project.core.port.out.MemberEmailPort;

@FeignClient(name = "auth-server", configuration = InternalFeignConfiguration.class)
public interface MemberCredentialFeignClient extends MemberEmailPort {
    @GetMapping("/internal/member-credentials/{memberGuid}/email")
    String findEmailValue(@PathVariable String memberGuid);
    @Override default Optional<String> findEmail(String memberGuid) {
        return Optional.ofNullable(findEmailValue(memberGuid));
    }
}
