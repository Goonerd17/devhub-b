package teamdevhub.devhub.community.outbound.member;

import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;
import teamdevhub.devhub.community.core.port.out.MemberEmailPort;

@FeignClient(name = "auth-server", configuration = InternalFeignConfiguration.class)
public interface CommunityMemberEmailFeignClient extends MemberEmailPort {
    @GetMapping("/internal/member-credentials/{memberGuid}/email")
    String findEmailValue(@PathVariable String memberGuid);
    @Override default Optional<String> findEmail(String memberGuid) {
        return Optional.ofNullable(findEmailValue(memberGuid));
    }
}
