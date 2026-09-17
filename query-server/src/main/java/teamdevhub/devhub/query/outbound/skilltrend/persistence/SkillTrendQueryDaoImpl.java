package teamdevhub.devhub.query.outbound.skilltrend.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SkillTrendQueryDaoImpl implements SkillTrendQueryDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long countTotalProjects() {
        Long result = (Long) entityManager.createQuery(
                "SELECT COUNT(p) FROM ProjectEntity p WHERE p.deleted = false")
                .getSingleResult();
        return result != null ? result : 0L;
    }

    @Override
    public long countActiveUsers() {
        Long result = (Long) entityManager.createQuery(
                "SELECT COUNT(u) FROM UserEntity u WHERE u.deleted = false AND u.blocked = false")
                .getSingleResult();
        return result != null ? result : 0L;
    }

    @Override
    public double avgMannerDegree() {
        Double result = (Double) entityManager.createQuery(
                "SELECT AVG(u.mannerDegree) FROM UserEntity u WHERE u.deleted = false AND u.blocked = false")
                .getSingleResult();
        return result != null ? Math.round(result * 100.0) / 100.0 : 0.0;
    }

    @Override
    public long countNewSkills(LocalDate since) {
        Long result = (Long) entityManager.createQuery(
                "SELECT COUNT(DISTINCT ps.skillCd) FROM ProjectSkillEntity ps " +
                "JOIN ProjectEntity p ON p.projectGuid = ps.projectGuid " +
                "WHERE p.registeredDate >= :since AND p.deleted = false")
                .setParameter("since", since.atStartOfDay())
                .getSingleResult();
        return result != null ? result : 0L;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findTopDemandedSkills(int limit) {
        return entityManager.createQuery(
                "SELECT ps.skillCd, COUNT(ps) as cnt FROM ProjectSkillEntity ps " +
                "JOIN ProjectEntity p ON p.projectGuid = ps.projectGuid " +
                "WHERE p.deleted = false " +
                "GROUP BY ps.skillCd ORDER BY cnt DESC")
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findTopPopularPositions(int limit) {
        return entityManager.createQuery(
                "SELECT pr.positionCd, COUNT(pr) as cnt FROM ProjectRequirementEntity pr " +
                "JOIN ProjectEntity p ON p.projectGuid = pr.projectGuid " +
                "WHERE p.deleted = false " +
                "GROUP BY pr.positionCd ORDER BY cnt DESC")
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findSkillDemandByPosition() {
        return entityManager.createQuery(
                "SELECT pr.positionCd, ps.skillCd, COUNT(ps) as cnt " +
                "FROM ProjectRequirementEntity pr " +
                "JOIN ProjectSkillEntity ps ON ps.projectGuid = pr.projectGuid " +
                "JOIN ProjectEntity p ON p.projectGuid = pr.projectGuid " +
                "WHERE p.deleted = false " +
                "GROUP BY pr.positionCd, ps.skillCd " +
                "ORDER BY pr.positionCd ASC, cnt DESC")
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findMonthlyTimeline(LocalDate since) {
        return entityManager.createQuery(
                "SELECT YEAR(p.registeredDate), MONTH(p.registeredDate), " +
                "SUM(CASE WHEN p.recruitmentStartDate <= CURRENT_DATE AND p.recruitmentEndDate >= CURRENT_DATE THEN 1 ELSE 0 END), " +
                "SUM(CASE WHEN p.progressStartDate <= CURRENT_DATE AND p.progressEndDate >= CURRENT_DATE THEN 1 ELSE 0 END) " +
                "FROM ProjectEntity p " +
                "WHERE p.deleted = false AND p.registeredDate >= :since " +
                "GROUP BY YEAR(p.registeredDate), MONTH(p.registeredDate) " +
                "ORDER BY YEAR(p.registeredDate) ASC, MONTH(p.registeredDate) ASC")
                .setParameter("since", since.atStartOfDay())
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findSkillDemandCounts(int limit) {
        return entityManager.createQuery(
                "SELECT ps.skillCd, COUNT(ps) as cnt FROM ProjectSkillEntity ps " +
                "JOIN ProjectEntity p ON p.projectGuid = ps.projectGuid " +
                "WHERE p.deleted = false " +
                "GROUP BY ps.skillCd ORDER BY cnt DESC")
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findSkillSupplyCounts(int limit) {
        return entityManager.createQuery(
                "SELECT us.skillCd, COUNT(us) as cnt FROM UserSkillEntity us " +
                "JOIN UserEntity u ON u.userGuid = us.userGuid " +
                "WHERE u.deleted = false " +
                "GROUP BY us.skillCd ORDER BY cnt DESC")
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findMarketableSkills() {
        return entityManager.createQuery(
                "SELECT up.positionCd, us.skillCd, COUNT(us) as cnt " +
                "FROM UserSkillEntity us " +
                "JOIN UserPositionEntity up ON up.userGuid = us.userGuid " +
                "JOIN UserEntity u ON u.userGuid = us.userGuid " +
                "WHERE u.deleted = false " +
                "GROUP BY up.positionCd, us.skillCd " +
                "ORDER BY up.positionCd ASC, cnt DESC")
                .getResultList();
    }
}
