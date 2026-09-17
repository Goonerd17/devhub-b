package teamdevhub.devhub.query.outbound.skilltrend.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.MarketableSkill;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.SkillDemandByPosition;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.SkillSupplyDemand;
import teamdevhub.devhub.query.core.skilltrend.port.out.LoadSkillDemandPort;
import teamdevhub.devhub.query.outbound.skilltrend.persistence.SkillTrendQueryDao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SkillTrendAnalyticsAdapter implements LoadSkillDemandPort {

    private final SkillTrendQueryDao skillTrendQueryDao;

    @Override
    public List<SkillDemandByPosition> loadSkillDemandByPosition(int limitPerPosition) {
        List<Object[]> rows = skillTrendQueryDao.findSkillDemandByPosition();
        Map<String, Integer> positionCountMap = new LinkedHashMap<>();
        List<SkillDemandByPosition> result = new ArrayList<>();

        for (Object[] row : rows) {
            String positionCd = (String) row[0];
            String skillCd = (String) row[1];
            long count = ((Number) row[2]).longValue();

            int currentCount = positionCountMap.getOrDefault(positionCd, 0);
            if (currentCount < limitPerPosition) {
                result.add(SkillDemandByPosition.of(positionCd, skillCd, count));
                positionCountMap.put(positionCd, currentCount + 1);
            }
        }

        return result;
    }

    @Override
    public List<SkillSupplyDemand> loadSkillSupplyDemand(int limit) {
        List<Object[]> demandRows = skillTrendQueryDao.findSkillDemandCounts(limit);
        List<Object[]> supplyRows = skillTrendQueryDao.findSkillSupplyCounts(limit);

        Map<String, Long> demandMap = new LinkedHashMap<>();
        for (Object[] row : demandRows) {
            demandMap.put((String) row[0], ((Number) row[1]).longValue());
        }

        Map<String, Long> supplyMap = new LinkedHashMap<>();
        for (Object[] row : supplyRows) {
            supplyMap.put((String) row[0], ((Number) row[1]).longValue());
        }

        return demandMap.keySet().stream()
                .map(skillCd -> SkillSupplyDemand.of(
                        skillCd,
                        demandMap.get(skillCd),
                        supplyMap.getOrDefault(skillCd, 0L)
                ))
                .toList();
    }

    @Override
    public List<MarketableSkill> loadMarketableSkills(int limitPerPosition) {
        List<Object[]> rows = skillTrendQueryDao.findMarketableSkills();
        Map<String, Integer> positionCountMap = new LinkedHashMap<>();
        List<MarketableSkill> result = new ArrayList<>();

        for (Object[] row : rows) {
            String positionCd = (String) row[0];
            String skillCd = (String) row[1];
            long count = ((Number) row[2]).longValue();

            int currentCount = positionCountMap.getOrDefault(positionCd, 0);
            if (currentCount < limitPerPosition) {
                result.add(MarketableSkill.of(positionCd, skillCd, count));
                positionCountMap.put(positionCd, currentCount + 1);
            }
        }

        return result;
    }
}
