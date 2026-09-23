package teamdevhub.devhub.community.outbound.member;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.shared.member.MemberCommunityProfile;
import teamdevhub.devhub.community.core.port.out.MemberProfilePort;
import teamdevhub.devhub.shared.member.MemberPublicProfile;
import teamdevhub.devhub.shared.member.MemberModerationProfile;
import teamdevhub.devhub.community.core.port.out.MemberModerationPort;
import teamdevhub.devhub.community.core.port.out.MemberEmailPort;

@Component
public class MemberHttpClient implements MemberProfilePort, MemberEmailPort, MemberModerationPort {
    private final CommunityMemberProfileFeignClient memberProfileClient;
    private final CommunityMemberModerationFeignClient memberModerationClient;
    private final CommunityMemberEmailFeignClient authClient;

    public MemberHttpClient(CommunityMemberProfileFeignClient memberProfileClient,
            CommunityMemberModerationFeignClient memberModerationClient,
            CommunityMemberEmailFeignClient authClient) {
        this.memberProfileClient = memberProfileClient;
        this.memberModerationClient = memberModerationClient;
        this.authClient = authClient;
    }

    @Override
    public List<MemberPublicProfile> findPublicProfilesByMemberGuids(List<String> memberGuids) {
        if (memberGuids.isEmpty()) return List.of();
        return memberProfileClient.findPublicProfilesByMemberGuids(memberGuids);
    }

    @Override
    public List<MemberCommunityProfile> findCommunityProfiles(List<String> memberGuids) {
        if (memberGuids.isEmpty()) return List.of();
        return memberProfileClient.findCommunityProfiles(memberGuids);
    }

    @Override
    public Optional<String> findEmail(String memberGuid) {
        return authClient.findEmail(memberGuid);
    }

    @Override
    public List<String> findMemberGuidsByStatus(String statusCode) {
        return memberModerationClient.findMemberGuidsByStatus(statusCode);
    }

    @Override
    public List<MemberModerationProfile> findModerationProfiles(List<String> memberGuids) {
        if (memberGuids.isEmpty()) return List.of();
        return memberModerationClient.findModerationProfiles(memberGuids);
    }
}
