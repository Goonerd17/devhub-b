package teamdevhub.devhub.community.outbound.member;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import teamdevhub.devhub.community.core.port.out.MemberProfilePort;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;
import teamdevhub.devhub.shared.member.MemberCommunityProfile;
import teamdevhub.devhub.shared.member.MemberPublicProfile;

@FeignClient(name = "member-server", contextId = "communityMemberProfileClient",
        configuration = InternalFeignConfiguration.class)
public interface CommunityMemberProfileFeignClient extends MemberProfilePort {
    @Override
    @GetMapping("/internal/members/public-profiles")
    List<MemberPublicProfile> findPublicProfilesByMemberGuids(@RequestParam List<String> memberGuids);

    @Override
    @GetMapping("/internal/members/community-profiles")
    List<MemberCommunityProfile> findCommunityProfiles(@RequestParam List<String> memberGuids);
}
