//package teamdevhub.devhub.project.outbound.project.persistence;
//
//import com.querydsl.core.Tuple;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.core.types.dsl.StringPath;
//import com.querydsl.jpa.JPAExpressions;
//import com.querydsl.jpa.JPQLQuery;
//import com.querydsl.jpa.impl.JPAQuery;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//import teamdevhub.devhub.project.core.project.domain.Project;
//import teamdevhub.devhub.project.core.project.port.in.command.SearchProjectListCommand;
//import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectEntity;
//import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectRequirementEntity;
//import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectSkillEntity;
//import teamdevhub.devhub.project.outbound.project.adapter.mapper.ProjectMapper;
//
//import java.util.*;
//
//import static teamdevhub.devhub.project.outbound.project.adapter.entity.QProjectEntity.projectEntity;
//import static teamdevhub.devhub.project.outbound.project.adapter.entity.QProjectLikeEntity.projectLikeEntity;
//import static teamdevhub.devhub.project.outbound.project.adapter.entity.QProjectRequirementEntity.projectRequirementEntity;
//import static teamdevhub.devhub.project.outbound.project.adapter.entity.QProjectSkillEntity.projectSkillEntity;
//
//@Repository
//@RequiredArgsConstructor
//public class ProjectQueryDaoImpl implements ProjectQueryDao {
//
//    private final JPAQueryFactory queryFactory;
//
//    @Override
//    public Page<Project> listProject(SearchProjectListCommand searchProjectListCommand, Pageable pageable) {
//
//        BooleanExpression[] projectCond = projectIn(searchProjectListCommand);
//        BooleanExpression[] skillCond = skillIn(searchProjectListCommand);
//        BooleanExpression[] positionCond = positionIn(searchProjectListCommand);
//        BooleanExpression[] allConditions = combine(projectCond, skillCond, positionCond);
//
//        JPAQuery<?> commonQuery = queryFactory
//                .from(projectEntity)
//                .leftJoin(projectSkillEntity).on(projectEntity.projectGuid.eq(projectSkillEntity.projectGuid))
//                .leftJoin(projectRequirementEntity).on(projectEntity.projectGuid.eq(projectRequirementEntity.projectGuid))
//                .where(allConditions);
//
//        List<String> projectGuidList = commonQuery
//                .select(projectEntity.projectGuid)
//                .distinct()
//                .fetch();
//
//        JPQLQuery<Long> likeCountSubQuery = JPAExpressions
//                .select(projectLikeEntity.count())
//                .from(projectLikeEntity)
//                .where(projectLikeEntity.projectGuid.eq(projectEntity.projectGuid));
//
//        List<Tuple> projects = queryFactory
//                .select(projectEntity,
//                        likeCountSubQuery)
//                .from(projectEntity)
//                .where(projectEntity.projectGuid.in(projectGuidList))
//                .fetch();
//        List<ProjectSkillEntity> allSkills = queryFactory
//                .selectFrom(projectSkillEntity)
//                .where(projectSkillEntity.projectGuid.in(projectGuidList))
//                .fetch();
//        List<ProjectRequirementEntity> allRequirements = queryFactory
//                .selectFrom(projectRequirementEntity)
//                .where(projectRequirementEntity.projectGuid.in(projectGuidList))
//                .fetch();
//        Long total = Optional.ofNullable(commonQuery
//                .select(projectEntity.countDistinct())
//                .fetchOne()).orElse(0L);
//
//        Map<String, List<String>> skillMap = ProjectMapper.skillEntityToMapSkill(allSkills);
//        Map<String, List<ProjectRequirementEntity>> requirementsMap = ProjectMapper.requirementEntityToMapRequirement(allRequirements);
//
//        List<Project> content = projects.stream().map(tuple -> {
//            ProjectEntity project = tuple.get(projectEntity);
//            return ProjectMapper.toProject(
//                    Objects.requireNonNull(project),
//                    skillMap.get(project.getProjectGuid()),
//                    requirementsMap.get(project.getProjectGuid()),
//                    Objects.requireNonNull(tuple.get(likeCountSubQuery)).toString()
//                    );
//        }).toList();
//
//        return new PageImpl<>(content, pageable, total);
//    }
//
//	private BooleanExpression conditionIn(StringPath path, List<String> values) {
//		return (values == null || values.isEmpty()) ? null : path.in(values);
//	}
//
//	private BooleanExpression[] projectIn(SearchProjectListCommand searchProjectListCommand) {
//		return new BooleanExpression[]{
//				conditionIn(projectEntity.recruitmentTypeCd, searchProjectListCommand.projectRecruitTypeList()),
//		};
//	}
//
//	private BooleanExpression[] skillIn(SearchProjectListCommand searchProjectListCommand) {
//		return new BooleanExpression[]{
//				conditionIn(projectSkillEntity.skillCd, searchProjectListCommand.skillCodeList()),
//		};
//	}
//
//	private BooleanExpression[] positionIn(SearchProjectListCommand searchProjectListCommand) {
//		return new BooleanExpression[]{
//				conditionIn(projectRequirementEntity.positionCd, searchProjectListCommand.positionCodeList()),
//		};
//	}
//
//	private BooleanExpression[] combine(BooleanExpression[]... arrays) {
//		return Arrays.stream(arrays)
//				.flatMap(Arrays::stream)
//				.filter(Objects::nonNull)
//				.toArray(BooleanExpression[]::new);
//	}
//
//        @Override
//	public Project findProjectDetailByGuid(String projectGuid) {
//
//		List<Tuple> rows = queryFactory
//				.select(
//						projectEntity,
//						projectSkillEntity,
//						projectRequirementEntity,
//						JPAExpressions
//								.select(projectLikeEntity.count().stringValue())
//								.from(projectLikeEntity)
//								.where(projectLikeEntity.projectGuid.eq(projectEntity.projectGuid))
//				)
//				.from(projectEntity)
//				.leftJoin(projectSkillEntity)
//				.on(projectEntity.projectGuid.eq(projectSkillEntity.projectGuid))
//				.leftJoin(projectRequirementEntity)
//				.on(projectEntity.projectGuid.eq(projectRequirementEntity.projectGuid))
//				.where(
//						projectEntity.projectGuid.eq(projectGuid),
//						projectEntity.deleted.eq(false)
//				)
//				.fetch();
//
//		if (rows.isEmpty()) {
//			throw new IllegalArgumentException("프로젝트가 존재하지 않습니다. projectGuid=" + projectGuid);
//		}
//
//		ProjectEntity project = rows.get(0).get(projectEntity);
//
//		List<ProjectSkillEntity> skillEntities = rows.stream()
//				.map(row -> row.get(projectSkillEntity))
//				.filter(Objects::nonNull)
//				.distinct()
//				.toList();
//
//		List<ProjectRequirementEntity> requirementEntities = rows.stream()
//				.map(row -> row.get(projectRequirementEntity))
//				.filter(Objects::nonNull)
//				.distinct()
//				.toList();
//
//		String likeCount = rows.get(0).get(3, String.class);
//
//		return ProjectMapper.toProjectDetail(
//				project,
//				skillEntities,
//				requirementEntities,
//				likeCount
//		);
//	}
//}