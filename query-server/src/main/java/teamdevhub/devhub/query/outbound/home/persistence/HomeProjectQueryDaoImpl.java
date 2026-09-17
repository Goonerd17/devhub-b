package teamdevhub.devhub.query.outbound.home.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static teamdevhub.devhub.project.outbound.project.adapter.entity.QProjectEntity.projectEntity;

@Repository
@RequiredArgsConstructor
public class HomeProjectQueryDaoImpl implements HomeProjectQueryDao {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<HomeProjectDto> findHomeProjects(LocalDate today, Pageable pageable) {
        return queryFactory
                .select(Projections.constructor(HomeProjectDto.class,
                        projectEntity.projectGuid,
                        projectEntity.title,
                        projectEntity.category,
                        projectEntity.username,
                        projectEntity.imageFileGuid,
                        projectEntity.recruitmentStartDate,
                        projectEntity.recruitmentEndDate,
                        projectEntity.registeredDate,
                        projectEntity.capacityClosed
                ))
                .from(projectEntity)
                .where(
                        projectEntity.deleted.isFalse(),
                        projectEntity.capacityClosed.isFalse(),
                        projectEntity.recruitmentEndDate.isNull()
                                .or(projectEntity.recruitmentEndDate.goe(today))
                )
                .orderBy(
                        new CaseBuilder()
                                .when(projectEntity.recruitmentStartDate.loe(today)
                                        .and(projectEntity.recruitmentEndDate.goe(today)))
                                .then(0)
                                .otherwise(1)
                                .asc(),
                        projectEntity.registeredDate.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
