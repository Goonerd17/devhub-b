package teamdevhub.devhub.readmodel.core.home.port.out;

import teamdevhub.devhub.readmodel.core.home.port.in.query.HomeBoardQuery;

import java.util.List;

public interface LoadHomeBoardPort {

    List<HomeBoardResult> loadPopularBoards(HomeBoardQuery query);

    record HomeBoardResult(
            String boardGuid,
            String title,
            String categoryCd,
            String username,
            int viewCount,
            long likeCount,
            String registeredDate
    ) {}
}
