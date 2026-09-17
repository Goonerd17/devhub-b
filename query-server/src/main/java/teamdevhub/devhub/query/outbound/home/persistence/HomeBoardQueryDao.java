package teamdevhub.devhub.query.outbound.home.persistence;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface HomeBoardQueryDao {

    List<HomeBoardDto> findPopularBoards(Pageable pageable, boolean sortByLike);

    record HomeBoardDto(
            String boardGuid,
            String title,
            String categoryCd,
            String username,
            Integer viewCount,
            Long likeCount,
            LocalDateTime registeredDate
    ) {}
}
