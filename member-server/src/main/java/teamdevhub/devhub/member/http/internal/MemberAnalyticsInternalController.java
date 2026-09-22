package teamdevhub.devhub.member.http.internal;

import java.util.List;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.shared.analytics.*;

@RestController
@RequestMapping("/internal/analytics/members")
@RequiredArgsConstructor
public class MemberAnalyticsInternalController {
    private final EntityManager entityManager;

    @GetMapping("/active/count")
    public long activeCount() { return (Long) entityManager.createQuery("SELECT COUNT(u) FROM UserEntity u WHERE u.deleted = false AND u.blocked = false").getSingleResult(); }

    @GetMapping("/manner-degree/average")
    public double averageMannerDegree() {
        Double value = (Double) entityManager.createQuery("SELECT AVG(u.mannerDegree) FROM UserEntity u WHERE u.deleted = false AND u.blocked = false").getSingleResult();
        return value == null ? 0 : Math.round(value * 100.0) / 100.0;
    }

    @GetMapping("/supplied-skills")
    public List<CodeCount> suppliedSkills(@RequestParam int limit) {
        return entityManager.createQuery("SELECT us.skillCd, COUNT(us) FROM UserSkillEntity us JOIN UserEntity u ON u.userGuid = us.userGuid WHERE u.deleted = false GROUP BY us.skillCd ORDER BY COUNT(us) DESC", Object[].class)
                .setMaxResults(limit).getResultList().stream().map(row -> new CodeCount((String) row[0], ((Number) row[1]).longValue())).toList();
    }

    @GetMapping("/marketable-skills")
    public List<PositionSkillCount> marketableSkills() {
        return entityManager.createQuery("SELECT up.positionCd, us.skillCd, COUNT(us) FROM UserSkillEntity us JOIN UserPositionEntity up ON up.userGuid = us.userGuid JOIN UserEntity u ON u.userGuid = us.userGuid WHERE u.deleted = false GROUP BY up.positionCd, us.skillCd ORDER BY up.positionCd, COUNT(us) DESC", Object[].class)
                .getResultList().stream().map(row -> new PositionSkillCount((String) row[0], (String) row[1], ((Number) row[2]).longValue())).toList();
    }
}
