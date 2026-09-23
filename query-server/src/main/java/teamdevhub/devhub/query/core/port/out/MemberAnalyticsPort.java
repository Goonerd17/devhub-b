package teamdevhub.devhub.query.core.port.out;
import java.util.List; import teamdevhub.devhub.shared.analytics.*;
public interface MemberAnalyticsPort { long countActiveMembers(); double averageMannerDegree(); List<CodeCount> suppliedSkills(int limit); List<PositionSkillCount> marketableSkills(); }
