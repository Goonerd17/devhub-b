package teamdevhub.devhub.readmodel.core.skilltrend.domain.vo;

import lombok.Builder;

@Builder
public record ProjectTimeline(
        int year,
        int month,
        long recruitmentCnt,
        long progressCnt
) {

    public static ProjectTimeline of(int year, int month, long recruitmentCnt, long progressCnt) {
        return ProjectTimeline.builder()
                .year(year)
                .month(month)
                .recruitmentCnt(recruitmentCnt)
                .progressCnt(progressCnt)
                .build();
    }
}
