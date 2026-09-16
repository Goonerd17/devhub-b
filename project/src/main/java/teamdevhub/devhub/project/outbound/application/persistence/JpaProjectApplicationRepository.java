package teamdevhub.devhub.project.outbound.application.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.project.outbound.application.adapter.entity.ProjectApplicationEntity;

public interface JpaProjectApplicationRepository extends JpaRepository<ProjectApplicationEntity, String> {

	Page<ProjectApplicationEntity> findByRequirementGuidIn(List<String> requirementGuids, Pageable pageable);

	Optional<ProjectApplicationEntity> findByApplicationGuid(String applicationGuid);

	Page<ProjectApplicationEntity> findByApplicantGuid(String userGuid, PageRequest of);

	// 취소된 지원은 목록/집계에서 제외한다 - isCanceled는 Lombok의 is-접두 boolean 필드라
	// Spring Data 메서드명 파생 시 프로퍼티명이 모호해질 수 있어 JPQL로 명시한다.
	@Query("SELECT a FROM ProjectApplicationEntity a WHERE a.applicantGuid = :applicantGuid AND a.isCanceled = false")
	Page<ProjectApplicationEntity> findByApplicantGuidAndNotCanceled(@Param("applicantGuid") String applicantGuid, Pageable pageable);

	@Query("SELECT a FROM ProjectApplicationEntity a WHERE a.requirementGuid IN :requirementGuids AND a.isCanceled = false")
	Page<ProjectApplicationEntity> findByRequirementGuidInAndNotCanceled(@Param("requirementGuids") List<String> requirementGuids, Pageable pageable);

	@Query("SELECT a.requirementGuid, COUNT(a) FROM ProjectApplicationEntity a "
		+ "WHERE a.requirementGuid IN :requirementGuids AND a.statusCd = :statusCd AND a.isCanceled = false "
		+ "GROUP BY a.requirementGuid")
	List<Object[]> countByRequirementGuidsAndStatusCd(@Param("requirementGuids") List<String> requirementGuids, @Param("statusCd") String statusCd);

}
