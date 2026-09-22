package teamdevhub.devhub.project.http.internal;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.shared.analytics.*;

@RestController
@RequestMapping("/internal/analytics/projects")
@RequiredArgsConstructor
public class ProjectAnalyticsInternalController {
    private final EntityManager entityManager;

    @GetMapping("/count")
    public long count() { return (Long) entityManager.createQuery("SELECT COUNT(p) FROM ProjectEntity p WHERE p.deleted = false").getSingleResult(); }

    @GetMapping("/new-skills/count")
    public long newSkills(@RequestParam LocalDate since) {
        return (Long) entityManager.createQuery("SELECT COUNT(DISTINCT ps.skillCd) FROM ProjectSkillEntity ps JOIN ProjectEntity p ON p.projectGuid = ps.projectGuid WHERE p.registeredDate >= :since AND p.deleted = false")
                .setParameter("since", since.atStartOfDay()).getSingleResult();
    }

    @GetMapping("/demanded-skills")
    public List<CodeCount> demandedSkills(@RequestParam int limit) { return codeCounts("SELECT ps.skillCd, COUNT(ps) FROM ProjectSkillEntity ps JOIN ProjectEntity p ON p.projectGuid = ps.projectGuid WHERE p.deleted = false GROUP BY ps.skillCd ORDER BY COUNT(ps) DESC", limit); }

    @GetMapping("/popular-positions")
    public List<CodeCount> popularPositions(@RequestParam int limit) { return codeCounts("SELECT pr.positionCd, COUNT(pr) FROM ProjectRequirementEntity pr JOIN ProjectEntity p ON p.projectGuid = pr.projectGuid WHERE p.deleted = false GROUP BY pr.positionCd ORDER BY COUNT(pr) DESC", limit); }

    @GetMapping("/skill-demand-by-position")
    public List<PositionSkillCount> demandByPosition() {
        return entityManager.createQuery("SELECT pr.positionCd, ps.skillCd, COUNT(ps) FROM ProjectRequirementEntity pr JOIN ProjectSkillEntity ps ON ps.projectGuid = pr.projectGuid JOIN ProjectEntity p ON p.projectGuid = pr.projectGuid WHERE p.deleted = false GROUP BY pr.positionCd, ps.skillCd ORDER BY pr.positionCd ASC, COUNT(ps) DESC", Object[].class)
                .getResultList().stream().map(row -> new PositionSkillCount((String) row[0], (String) row[1], ((Number) row[2]).longValue())).toList();
    }

    @GetMapping("/monthly-timeline")
    public List<MonthlyProjectCount> timeline(@RequestParam LocalDate since) {
        return entityManager.createQuery("SELECT YEAR(p.registeredDate), MONTH(p.registeredDate), SUM(CASE WHEN p.recruitmentStartDate <= CURRENT_DATE AND p.recruitmentEndDate >= CURRENT_DATE THEN 1 ELSE 0 END), SUM(CASE WHEN p.progressStartDate <= CURRENT_DATE AND p.progressEndDate >= CURRENT_DATE THEN 1 ELSE 0 END) FROM ProjectEntity p WHERE p.deleted = false AND p.registeredDate >= :since GROUP BY YEAR(p.registeredDate), MONTH(p.registeredDate) ORDER BY YEAR(p.registeredDate), MONTH(p.registeredDate)", Object[].class)
                .setParameter("since", since.atStartOfDay()).getResultList().stream()
                .map(row -> new MonthlyProjectCount(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue(),
                        number(row[2]),
                        number(row[3])))
                .toList();
    }

    private List<CodeCount> codeCounts(String jpql, int limit) {
        return entityManager.createQuery(jpql, Object[].class).setMaxResults(limit).getResultList().stream()
                .map(row -> new CodeCount((String) row[0], ((Number) row[1]).longValue())).toList();
    }

    private long number(Object value) {
        return value == null ? 0 : ((Number) value).longValue();
    }
}
