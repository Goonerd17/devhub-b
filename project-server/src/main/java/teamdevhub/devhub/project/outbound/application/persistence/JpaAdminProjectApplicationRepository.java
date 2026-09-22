package teamdevhub.devhub.project.outbound.application.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.project.outbound.application.adapter.entity.ProjectApplicationEntity;

public interface JpaAdminProjectApplicationRepository extends JpaRepository<ProjectApplicationEntity, String> {

	@Query("""
			select T1
			from ProjectApplicationEntity T1
			left join ProjectRequirementEntity ST1 on ST1.projectRequirementGuid = T1.requirementGuid
		    where 1 = 1
		    and (:positionCd is null or ST1.positionCd = :positionCd)
		    and (:levelCd is null or ST1.levelCd = :levelCd)
		    and (:approvalStatusCd is null or T1.statusCd = :approvalStatusCd)
			""")
	Page<ProjectApplicationEntity> getApplicationsByProjectGuid(@Param("positionCd")String positionCd, @Param("levelCd")String levelCd,
			@Param("approvalStatusCd")String approvalStatusCd, Pageable pageable);

}
