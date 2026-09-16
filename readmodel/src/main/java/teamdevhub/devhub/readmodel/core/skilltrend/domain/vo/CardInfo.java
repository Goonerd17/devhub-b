package teamdevhub.devhub.readmodel.core.skilltrend.domain.vo;

import lombok.Builder;

@Builder
public record CardInfo(
        long projectTotalCnt,
        long activeUserCnt,
        double matchingAvg,
        long newSkillCnt
) {

    public static CardInfo of(long projectTotalCnt, long activeUserCnt, double matchingAvg, long newSkillCnt) {
        return CardInfo.builder()
                .projectTotalCnt(projectTotalCnt)
                .activeUserCnt(activeUserCnt)
                .matchingAvg(matchingAvg)
                .newSkillCnt(newSkillCnt)
                .build();
    }
}
