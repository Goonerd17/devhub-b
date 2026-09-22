package teamdevhub.devhub.query.outbound.skilltrend.http;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.analytics.CodeCount;
import teamdevhub.devhub.shared.analytics.MemberAnalyticsGateway;
import teamdevhub.devhub.shared.analytics.PositionSkillCount;

@Component
public class MemberAnalyticsHttpClient implements MemberAnalyticsGateway {
    private final RestClient client;

    public MemberAnalyticsHttpClient(RestClient.Builder builder,
            @Value("${services.member.base-url:http://member-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @Override
    public long countActiveMembers() {
        Long result = client.get().uri("/internal/analytics/members/active/count").retrieve().body(Long.class);
        return result == null ? 0 : result;
    }

    @Override
    public double averageMannerDegree() {
        Double result = client.get().uri("/internal/analytics/members/manner-degree/average").retrieve().body(Double.class);
        return result == null ? 0 : result;
    }

    @Override
    public List<CodeCount> suppliedSkills(int limit) {
        List<CodeCount> result = client.get().uri(uri -> uri.path("/internal/analytics/members/supplied-skills")
                .queryParam("limit", limit).build()).retrieve().body(new ParameterizedTypeReference<>() {});
        return result == null ? List.of() : result;
    }

    @Override
    public List<PositionSkillCount> marketableSkills() {
        List<PositionSkillCount> result = client.get().uri("/internal/analytics/members/marketable-skills")
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return result == null ? List.of() : result;
    }
}
