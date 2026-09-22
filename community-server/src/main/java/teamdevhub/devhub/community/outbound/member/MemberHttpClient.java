package teamdevhub.devhub.community.outbound.member;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.member.MemberCommunityProfile;
import teamdevhub.devhub.shared.member.MemberProfileQuery;
import teamdevhub.devhub.shared.member.MemberPublicProfile;
import teamdevhub.devhub.shared.member.MemberModerationProfile;
import teamdevhub.devhub.shared.member.MemberModerationQuery;
import teamdevhub.devhub.shared.security.MemberEmailQuery;

@Component
public class MemberHttpClient implements MemberProfileQuery, MemberEmailQuery, MemberModerationQuery {
    private final RestClient memberClient;
    private final RestClient authClient;

    public MemberHttpClient(RestClient.Builder builder,
            @Value("${services.member.base-url:http://member-server}") String memberBaseUrl,
            @Value("${services.auth.base-url:http://auth-server}") String authBaseUrl) {
        this.memberClient = builder.clone().baseUrl(memberBaseUrl).build();
        this.authClient = builder.clone().baseUrl(authBaseUrl).build();
    }

    @Override
    public List<MemberPublicProfile> findPublicProfilesByMemberGuids(List<String> memberGuids) {
        if (memberGuids.isEmpty()) return List.of();
        List<MemberPublicProfile> response = memberClient.get()
                .uri(uri -> uri.path("/internal/members/public-profiles")
                        .queryParam("memberGuids", memberGuids.toArray()).build())
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return response == null ? List.of() : response;
    }

    @Override
    public List<MemberCommunityProfile> findCommunityProfiles(List<String> memberGuids) {
        if (memberGuids.isEmpty()) return List.of();
        List<MemberCommunityProfile> response = memberClient.get()
                .uri(uri -> uri.path("/internal/members/community-profiles")
                        .queryParam("memberGuids", memberGuids.toArray()).build())
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return response == null ? List.of() : response;
    }

    @Override
    public Optional<String> findEmail(String memberGuid) {
        try {
            return Optional.ofNullable(authClient.get()
                    .uri("/internal/member-credentials/{memberGuid}/email", memberGuid)
                    .retrieve().body(String.class));
        } catch (HttpClientErrorException.NotFound ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<String> findMemberGuidsByStatus(String statusCode) {
        List<String> response = memberClient.get()
                .uri("/internal/members/moderation-status/{statusCode}", statusCode)
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return response == null ? List.of() : response;
    }

    @Override
    public List<MemberModerationProfile> findModerationProfiles(List<String> memberGuids) {
        if (memberGuids.isEmpty()) return List.of();
        List<MemberModerationProfile> response = memberClient.get()
                .uri(uri -> uri.path("/internal/members/moderation-profiles")
                        .queryParam("memberGuids", memberGuids.toArray()).build())
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return response == null ? List.of() : response;
    }
}
