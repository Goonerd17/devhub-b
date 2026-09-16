package teamdevhub.devhub.readmodel.outbound.home.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static teamdevhub.devhub.community.outbound.board.adapter.entity.QBoardEntity.boardEntity;
import static teamdevhub.devhub.community.outbound.board.adapter.entity.QBoardLikeEntity.boardLikeEntity;
import static teamdevhub.devhub.member.outbound.user.adapter.entity.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class HomeBoardQueryDaoImpl implements HomeBoardQueryDao {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<HomeBoardDto> findPopularBoards(Pageable pageable, boolean sortByLike) {
        return queryFactory
                .select(Projections.constructor(HomeBoardDto.class,
                        boardEntity.boardGuid,
                        boardEntity.title,
                        boardEntity.categoryCd,
                        userEntity.username,
                        boardEntity.viewCount,
                        boardLikeEntity.boardLikeGuid.count(),
                        boardEntity.registeredDate
                ))
                .from(boardEntity)
                .join(userEntity).on(boardEntity.userGuid.eq(userEntity.userGuid))
                .leftJoin(boardLikeEntity).on(boardLikeEntity.boardGuid.eq(boardEntity.boardGuid))
                .groupBy(
                        boardEntity.boardGuid,
                        boardEntity.title,
                        boardEntity.categoryCd,
                        userEntity.username,
                        boardEntity.viewCount,
                        boardEntity.registeredDate
                )
                .orderBy(sortByLike
                        ? boardLikeEntity.boardLikeGuid.count().desc()
                        : boardEntity.viewCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
