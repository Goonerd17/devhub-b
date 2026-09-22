package teamdevhub.devhub.shared.analytics;

import java.util.List;

public interface MemberAnalyticsGateway {
    long countActiveMembers();
    double averageMannerDegree();
    List<CodeCount> suppliedSkills(int limit);
    List<PositionSkillCount> marketableSkills();
}
