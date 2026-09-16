package teamdevhub.devhub.member.outbound.user.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

import static teamdevhub.devhub.member.outbound.user.adapter.entity.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class UserQueryDaoImpl implements UserQueryDao {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserEntity> listUser(SearchUserCommand searchUserCommand, Pageable pageable) {
        List<UserEntity> content = queryFactory
                .selectFrom(userEntity)
                .where(
                        blockedCondition(searchUserCommand.blocked()),
                        joinedFromCondition(searchUserCommand.joinedFrom()),
                        joinedToCondition(searchUserCommand.joinedTo()),
                        usernameCondition(searchUserCommand.username()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(userEntity.registeredDate.desc())
                .fetch();

        Long total = queryFactory
                .select(userEntity.count())
                .from(userEntity)
                .where(
                        blockedCondition(searchUserCommand.blocked()),
                        joinedFromCondition(searchUserCommand.joinedFrom()),
                        joinedToCondition(searchUserCommand.joinedTo()),
                        usernameCondition(searchUserCommand.username()))
                .fetchOne();

        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression blockedCondition(Boolean blocked) {
        if (blocked == null) {
            return null;
        }
        return userEntity.blocked.eq(blocked);
    }

    private BooleanExpression joinedFromCondition(LocalDateTime joinedFrom) {
        if (joinedFrom == null) {
            return null;
        }
        return userEntity.registeredDate.goe(joinedFrom);
    }

    private BooleanExpression joinedToCondition(LocalDateTime joinedTo) {
        if (joinedTo == null) {
            return null;
        }
        return userEntity.registeredDate.loe(joinedTo);
    }

    private BooleanExpression usernameCondition(String keyword) {
        if (keyword == null) {
            return null;
        }
        return userEntity.username.containsIgnoreCase(keyword);
    }
}