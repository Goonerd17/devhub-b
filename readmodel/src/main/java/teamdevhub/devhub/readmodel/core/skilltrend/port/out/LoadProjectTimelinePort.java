package teamdevhub.devhub.readmodel.core.skilltrend.port.out;

import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.ProjectTimeline;

import java.util.List;

public interface LoadProjectTimelinePort {

    List<ProjectTimeline> loadMonthlyTimeline(int months);
}
