package teamdevhub.devhub.community.http.internal;

import static teamdevhub.devhub.community.outbound.board.adapter.entity.QBoardEntity.boardEntity;
import static teamdevhub.devhub.community.outbound.board.adapter.entity.QBoardLikeEntity.boardLikeEntity;

import java.util.List;
import java.util.Map;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.shared.home.HomeBoardView;
import teamdevhub.devhub.community.core.port.out.MemberProfilePort;
import teamdevhub.devhub.shared.member.MemberPublicProfile;

@RestController
@RequestMapping("/internal/home/boards")
@RequiredArgsConstructor
public class HomeBoardInternalController {
    private final JPAQueryFactory queryFactory;
    private final MemberProfilePort memberProfileQuery;

    @GetMapping
    public List<HomeBoardView> popular(@RequestParam int limit, @RequestParam boolean sortByLike) {
        List<BoardProjection> boards = queryFactory.select(Projections.constructor(BoardProjection.class,
                        boardEntity.boardGuid, boardEntity.title, boardEntity.categoryCd, boardEntity.userGuid,
                        boardEntity.viewCount, boardLikeEntity.boardLikeGuid.count(), boardEntity.registeredDate))
                .from(boardEntity)
                .leftJoin(boardLikeEntity).on(boardLikeEntity.boardGuid.eq(boardEntity.boardGuid))
                .groupBy(boardEntity.boardGuid, boardEntity.title, boardEntity.categoryCd, boardEntity.userGuid,
                        boardEntity.viewCount, boardEntity.registeredDate)
                .orderBy(sortByLike ? boardLikeEntity.boardLikeGuid.count().desc() : boardEntity.viewCount.desc())
                .limit(limit).fetch();
        Map<String, String> names = memberProfileQuery.findPublicProfilesByMemberGuids(
                        boards.stream().map(BoardProjection::userGuid).distinct().toList()).stream()
                .collect(java.util.stream.Collectors.toMap(MemberPublicProfile::memberGuid,
                        MemberPublicProfile::displayName));
        return boards.stream().map(board -> new HomeBoardView(board.boardGuid(), board.title(), board.categoryCd(),
                names.getOrDefault(board.userGuid(), ""), board.viewCount() == null ? 0 : board.viewCount(),
                board.likeCount() == null ? 0 : board.likeCount(), board.registeredDate())).toList();
    }

    public record BoardProjection(String boardGuid, String title, String categoryCd, String userGuid,
            Integer viewCount, Long likeCount, java.time.LocalDateTime registeredDate) {
    }
}
