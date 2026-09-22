package teamdevhub.devhub.project.outbound.member;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.member.MemberApplicationProfile;
import teamdevhub.devhub.shared.member.MemberProjectOwner;
import teamdevhub.devhub.shared.member.ProjectMemberQuery;

@Component
public class ProjectMemberHttpClient implements ProjectMemberQuery {
    private final RestClient client;

    public ProjectMemberHttpClient(RestClient.Builder builder,
            @Value("${services.member.base-url:http://member-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @Override
    public MemberProjectOwner findProjectOwner(String memberGuid) {
        return client.get().uri("/internal/members/{memberGuid}/project-owner", memberGuid)
                .retrieve().body(MemberProjectOwner.class);
    }

    @Override
    public Map<String, MemberApplicationProfile> findApplicationProfiles(List<String> memberGuids) {
        if (memberGuids.isEmpty()) return Map.of();
        Map<String, MemberApplicationProfile> response = client.get()
                .uri(uri -> uri.path("/internal/members/application-profiles")
                        .queryParam("memberGuids", memberGuids.toArray()).build())
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return response == null ? Map.of() : response;
    }

    @Override
    public Optional<Double> findReviewScore(String projectGuid, String revieweeGuid) {
        try {
            return Optional.ofNullable(client.get()
                    .uri(uri -> uri.path("/internal/members/{revieweeGuid}/review-score")
                            .queryParam("projectGuid", projectGuid).build(revieweeGuid))
                    .retrieve().body(Double.class));
        } catch (HttpClientErrorException.NotFound ignored) {
            return Optional.empty();
        }
    }
}
