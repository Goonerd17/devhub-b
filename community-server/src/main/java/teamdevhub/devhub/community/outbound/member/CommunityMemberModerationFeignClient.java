package teamdevhub.devhub.community.outbound.member;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import teamdevhub.devhub.community.core.port.out.MemberModerationPort;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;
import teamdevhub.devhub.shared.member.MemberModerationProfile;

@FeignClient(name = "member-server", contextId = "communityMemberModerationClient",
        configuration = InternalFeignConfiguration.class)
public interface CommunityMemberModerationFeignClient extends MemberModerationPort {
    @Override
    @GetMapping("/internal/members/moderation-status/{statusCode}")
    List<String> findMemberGuidsByStatus(@PathVariable String statusCode);

    @Override
    @GetMapping("/internal/members/moderation-profiles")
    List<MemberModerationProfile> findModerationProfiles(@RequestParam List<String> memberGuids);
}
