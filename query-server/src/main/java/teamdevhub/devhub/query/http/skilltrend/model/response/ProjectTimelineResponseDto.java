package teamdevhub.devhub.query.http.skilltrend.model.response;

import teamdevhub.devhub.query.core.skilltrend.domain.vo.ProjectTimeline;

public record ProjectTimelineResponseDto(
        int year,
        int month,
        long recruitmentCnt,
        long progressCnt
) {

    public static ProjectTimelineResponseDto from(ProjectTimeline timeline) {
        return new ProjectTimelineResponseDto(
                timeline.year(),
                timeline.month(),
                timeline.recruitmentCnt(),
                timeline.progressCnt()
        );
    }
}

