package teamdevhub.devhub.query.http.skilltrend.model.response;

import teamdevhub.devhub.query.core.skilltrend.domain.vo.CardInfo;

public record CardInfoResponseDto(
        long projectTotalCnt,
        long activeUserCnt,
        double matchingAvg,
        long newSkillCnt
) {

    public static CardInfoResponseDto from(CardInfo cardInfo) {
        return new CardInfoResponseDto(
                cardInfo.projectTotalCnt(),
                cardInfo.activeUserCnt(),
                cardInfo.matchingAvg(),
                cardInfo.newSkillCnt()
        );
    }
}

