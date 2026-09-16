package teamdevhub.devhub.administration.outbound.admin.code.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommonCodeQueryRepositoryImpl implements CommonCodeQueryRepository {

    private final JPAQueryFactory queryFactory;

}