package teamdevhub.devhub.query.outbound.skilltrend.http;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.analytics.CodeCount;
import teamdevhub.devhub.shared.analytics.MonthlyProjectCount;
import teamdevhub.devhub.shared.analytics.PositionSkillCount;
import teamdevhub.devhub.shared.analytics.ProjectAnalyticsGateway;

@Component
public class ProjectAnalyticsHttpClient implements ProjectAnalyticsGateway {
    private final RestClient client;

    public ProjectAnalyticsHttpClient(RestClient.Builder builder,
            @Value("${services.project.base-url:http://project-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @Override
    public long countProjects() {
        Long result = client.get().uri("/internal/analytics/projects/count").retrieve().body(Long.class);
        return result == null ? 0 : result;
    }

    @Override
    public long countNewSkills(LocalDate since) {
        Long result = client.get().uri(uri -> uri.path("/internal/analytics/projects/new-skills/count")
                .queryParam("since", since).build()).retrieve().body(Long.class);
        return result == null ? 0 : result;
    }

    @Override
    public List<CodeCount> demandedSkills(int limit) {
        return codeCounts("/internal/analytics/projects/demanded-skills", limit);
    }

    @Override
    public List<CodeCount> popularPositions(int limit) {
        return codeCounts("/internal/analytics/projects/popular-positions", limit);
    }

    @Override
    public List<PositionSkillCount> skillDemandByPosition() {
        List<PositionSkillCount> result = client.get().uri("/internal/analytics/projects/skill-demand-by-position")
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return result == null ? List.of() : result;
    }

    @Override
    public List<MonthlyProjectCount> monthlyTimeline(LocalDate since) {
        List<MonthlyProjectCount> result = client.get().uri(uri -> uri.path("/internal/analytics/projects/monthly-timeline")
                .queryParam("since", since).build()).retrieve().body(new ParameterizedTypeReference<>() {});
        return result == null ? List.of() : result;
    }

    private List<CodeCount> codeCounts(String path, int limit) {
        List<CodeCount> result = client.get().uri(uri -> uri.path(path).queryParam("limit", limit).build())
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return result == null ? List.of() : result;
    }
}
