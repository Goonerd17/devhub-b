package teamdevhub.devhub.fake.pure.application.port.in.usecase.skilltrend;

import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.CardInfo;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.PositionCount;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.ProjectTimeline;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.SkillCount;
import teamdevhub.devhub.readmodel.core.skilltrend.port.in.usecase.SkillTrendStatisticsUseCase;

import java.util.ArrayList;
import java.util.List;

public class FakeSkillTrendStatisticsUseCase implements SkillTrendStatisticsUseCase {

    private CardInfo cardInfo = CardInfo.of(0L, 0L, 0.0, 0L);
    private List<SkillCount> demandedSkills = new ArrayList<>();
    private List<PositionCount> popularPositions = new ArrayList<>();
    private List<ProjectTimeline> projectTimeline = new ArrayList<>();

    public void givenCardInfo(CardInfo info) {
        this.cardInfo = info;
    }

    public void givenDemandedSkills(List<SkillCount> skills) {
        this.demandedSkills = new ArrayList<>(skills);
    }

    public void givenPopularPositions(List<PositionCount> positions) {
        this.popularPositions = new ArrayList<>(positions);
    }

    public void givenProjectTimeline(List<ProjectTimeline> timeline) {
        this.projectTimeline = new ArrayList<>(timeline);
    }

    @Override
    public CardInfo getCardInfo() {
        return cardInfo;
    }

    @Override
    public List<SkillCount> getDemandedSkills(int limit) {
        return demandedSkills.stream().limit(limit).toList();
    }

    @Override
    public List<PositionCount> getPopularPositions(int limit) {
        return popularPositions.stream().limit(limit).toList();
    }

    @Override
    public List<ProjectTimeline> getProjectTimeline(int months) {
        return List.copyOf(projectTimeline);
    }
}
