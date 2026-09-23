package teamdevhub.devhub.query.outbound.skilltrend.http;

import java.util.List;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.shared.analytics.CodeCount;
import teamdevhub.devhub.query.core.port.out.MemberAnalyticsPort;
import teamdevhub.devhub.shared.analytics.PositionSkillCount;

@Component
public class MemberAnalyticsHttpClient implements MemberAnalyticsPort {
    private final MemberAnalyticsFeignClient client;

    public MemberAnalyticsHttpClient(MemberAnalyticsFeignClient client) {
        this.client = client;
    }

    @Override
    public long countActiveMembers() {
        return client.countActiveMembers();
    }

    @Override
    public double averageMannerDegree() {
        return client.averageMannerDegree();
    }

    @Override
    public List<CodeCount> suppliedSkills(int limit) {
        return client.suppliedSkills(limit);
    }

    @Override
    public List<PositionSkillCount> marketableSkills() {
        return client.marketableSkills();
    }
}
