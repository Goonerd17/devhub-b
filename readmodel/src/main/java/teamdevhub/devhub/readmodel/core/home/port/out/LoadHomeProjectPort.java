package teamdevhub.devhub.readmodel.core.home.port.out;

import teamdevhub.devhub.readmodel.core.home.port.in.query.HomeProjectQuery;

import java.util.List;

public interface LoadHomeProjectPort {

    List<HomeProjectResult> loadRecentProjects(HomeProjectQuery query);

    record HomeProjectResult(
            String projectGuid,
            String title,
            String category,
            String username,
            String imageFileGuid,
            String recruitmentStartDate,
            String recruitmentEndDate,
            String recruitStatus,
            boolean deleted
    ) {}
}
