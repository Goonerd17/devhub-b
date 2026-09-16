package teamdevhub.devhub.readmodel.core.skilltrend.domain.vo;

import lombok.Builder;

@Builder
public record SkillSupplyDemand(
        String skillCd,
        long demandCnt,
        long supplyCnt
) {

    public static SkillSupplyDemand of(String skillCd, long demandCnt, long supplyCnt) {
        return SkillSupplyDemand.builder()
                .skillCd(skillCd)
                .demandCnt(demandCnt)
                .supplyCnt(supplyCnt)
                .build();
    }
}
