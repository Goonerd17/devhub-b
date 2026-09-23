package teamdevhub.devhub.query.outbound.home.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.query.core.home.port.in.query.HomeBoardQuery;
import teamdevhub.devhub.query.core.home.port.out.LoadHomeBoardPort;
import teamdevhub.devhub.query.core.port.out.HomeBoardPort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HomeBoardAdapter implements LoadHomeBoardPort {

    private final HomeBoardPort homeBoardQueryDao;

    @Override
    public List<HomeBoardResult> loadPopularBoards(HomeBoardQuery query) {
        boolean sortByLike = HomeBoardQuery.BoardSortType.LIKE_COUNT.equals(query.sortType());
        return homeBoardQueryDao.findPopular(query.limit(), sortByLike)
                .stream()
                .map(dto -> new HomeBoardResult(
                        dto.boardGuid(),
                        dto.title(),
                        dto.categoryCd(),
                        dto.username(),
                        dto.viewCount(),
                        dto.likeCount(),
                        dto.registeredDate() != null ? dto.registeredDate().toString() : null
                ))
                .toList();
    }
}
