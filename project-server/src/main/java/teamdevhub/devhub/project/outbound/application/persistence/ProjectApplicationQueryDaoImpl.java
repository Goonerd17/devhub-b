package teamdevhub.devhub.project.outbound.application.persistence;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.project.outbound.application.adapter.entity.ProjectApplicationAnswerEntity;
import teamdevhub.devhub.project.outbound.application.adapter.entity.ProjectApplicationEntity;
import teamdevhub.devhub.project.outbound.application.adapter.mapper.ApplicationMapper;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.project.outbound.project.persistence.JpaProjectRequirementRepository;
import teamdevhub.devhub.shared.member.MemberApplicationProfile;
import teamdevhub.devhub.project.core.port.out.ProjectMemberPort;

@Repository
@RequiredArgsConstructor
public class ProjectApplicationQueryDaoImpl implements ProjectApplicationQueryDao {

	private final JpaProjectApplicationRepository jpaProjectApplicationRepository;
	private final JpaProjectApplicationAnswerRepository jpaProjectApplicationAnswerRepository;
	private final JpaProjectRequirementRepository jpaProjectRequirementRepository;
	private final ProjectMemberPort memberApplicationProfileQuery;

	@Override
	public Page<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, Pageable pageable) {

		// projectGuid → requirementGuid 목록 조회
		List<String> requirementGuidList = jpaProjectRequirementRepository
			.findByProjectGuid(projectGuid)
			.stream()
			.map(ProjectRequirementEntity::getProjectRequirementGuid)
			.toList();

		if (requirementGuidList.isEmpty()) {
			return new PageImpl<>(List.of(), pageable, 0);
		}

		// requirementGuid 목록으로 application 페이징 조회 (취소된 지원은 제외)
		Page<ProjectApplicationEntity> applicationPage =
			jpaProjectApplicationRepository.findByRequirementGuidInAndNotCanceled(requirementGuidList, pageable);

		List<ProjectApplicationEntity> applicationEntities = applicationPage.getContent();

		if (applicationEntities.isEmpty()) {
			return new PageImpl<>(List.of(), pageable, applicationPage.getTotalElements());
		}

		List<String> applicantGuidList = applicationEntities.stream()
			.map(ProjectApplicationEntity::getApplicantGuid)
			.distinct()
			.toList();

		List<String> reqGuidList = applicationEntities.stream()
			.map(ProjectApplicationEntity::getRequirementGuid)
			.distinct()
			.toList();

		// 지원자 유저 정보 조회
		Map<String, MemberApplicationProfile> userMap = memberApplicationProfileQuery.findApplicationProfiles(applicantGuidList);

		// 지원자 스킬 목록 조회 (코드 → 이름 변환)
		Map<String, List<String>> userSkillMap = userMap.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().skills()));

		// 모집 요건 정보 조회
		Map<String, ProjectRequirementEntity> requirementMap =
			jpaProjectRequirementRepository.findAllById(reqGuidList)
				.stream()
				.collect(Collectors.toMap(ProjectRequirementEntity::getProjectRequirementGuid, r -> r));

		List<ProjectApplication> content = applicationEntities.stream()
			.map(app -> {
				MemberApplicationProfile user = userMap.get(app.getApplicantGuid());
				ProjectRequirementEntity requirement = requirementMap.get(app.getRequirementGuid());
				List<String> skillList = userSkillMap.getOrDefault(app.getApplicantGuid(), List.of());

				if (user == null || requirement == null) return null;

				return ApplicationMapper.toApplication(app, user, requirement, skillList);
			})
			.filter(Objects::nonNull)
			.toList();

		return new PageImpl<>(content, pageable, applicationPage.getTotalElements());
	}

	@Override
	public ProjectApplication findApplicationByGuid(String applicationGuid) {
		ProjectApplicationEntity app = jpaProjectApplicationRepository
			.findByApplicationGuid(applicationGuid)
			.orElse(null);

		if (app == null) return null;

		MemberApplicationProfile user = memberApplicationProfileQuery.findApplicationProfiles(List.of(app.getApplicantGuid())).get(app.getApplicantGuid());

		ProjectRequirementEntity requirement = jpaProjectRequirementRepository
			.findByProjectRequirementGuid(app.getRequirementGuid())
			.orElse(null);

		List<String> skillList = user != null ? user.skills() : List.of();

		if (user == null || requirement == null) return null;

		return ApplicationMapper.toApplication(app, user, requirement, skillList);
	}

	@Override
	public List<ProjectApplicationAnswer> findAnswersByApplicationGuid(String applicationGuid) {
		List<ProjectApplicationAnswerEntity> answerEntities =
			jpaProjectApplicationAnswerRepository.findByApplicationGuid(applicationGuid);

		if (answerEntities.isEmpty()) return List.of();

		ProjectApplicationEntity app = jpaProjectApplicationRepository
			.findByApplicationGuid(applicationGuid)
			.orElse(null);

		if (app == null) return List.of();

		MemberApplicationProfile user = memberApplicationProfileQuery.findApplicationProfiles(List.of(app.getApplicantGuid())).get(app.getApplicantGuid());

		ProjectRequirementEntity requirement = jpaProjectRequirementRepository
			.findByProjectRequirementGuid(app.getRequirementGuid())
			.orElse(null);

		List<String> skillList = user != null ? user.skills() : List.of();

		return answerEntities.stream()
			.map(answer -> ProjectApplicationAnswer.builder()
				.applicationAnswerGuid(answer.getApplicationAnswerGuid())
				.applicationGuid(answer.getApplicationGuid())
				.projectApplicationFormGuid(answer.getProjectApplicationFormGuid())
				.applicationFormGuid(answer.getApplicationFormGuid())
				.projectGuid(answer.getProjectGuid())
				.fileGuid(answer.getFileGuid())
				.content(answer.getContent())
				.userName(user != null ? user.displayName() : null)
				//.email(user != null ? user.getEmail() : null)
				.mannerDegree(user != null ? user.mannerDegree() : 0)
				.userSkillList(skillList)
				.positionCd(requirement != null ? requirement.getPositionCd() : null)
				.introduction(user != null ? user.introduction() : null)
				.applyDate(app.getRegisteredDate() != null
					? app.getRegisteredDate().toLocalDate().toString() : null)
				.build()
			)
			.toList();
	}

	@Override
	public List<ProjectApplicationScore> findAcceptedByProjectGuid(String projectGuid) {
		Page<ProjectApplication> result = findApplicationsByProjectGuid(projectGuid, Pageable.unpaged());
		return result.getContent().stream()
				.filter(application -> "3302".equals(application.getStatusCd()))
				.map(application -> {
					Double score = memberApplicationProfileQuery.findReviewScore(projectGuid, application.getApplicantGuid())
								.orElse(null);
					return ProjectApplicationScore.toApplicationWithScore(application, score);
				})
				.toList();
	}
}
