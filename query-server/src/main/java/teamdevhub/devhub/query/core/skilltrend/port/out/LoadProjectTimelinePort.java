package teamdevhub.devhub.query.core.skilltrend.port.out;

import teamdevhub.devhub.query.core.skilltrend.domain.vo.ProjectTimeline;

import java.util.List;

public interface LoadProjectTimelinePort {

    List<ProjectTimeline> loadMonthlyTimeline(int months);
}
