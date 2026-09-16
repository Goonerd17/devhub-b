package teamdevhub.devhub.project.outbound.project.persistence;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectRequirementEntity;

public interface JpaProjectRequirementRepository extends JpaRepository<ProjectRequirementEntity, String> {

	@Query("""
			select pr.projectGuid
			from ProjectRequirementEntity pr
			where (:positionCodeList is null or pr.positionCd in :positionCodeList)
			and	  (:positionLevelCodeList is null or pr.levelCd in :positionLevelCodeList)
			""")
	List<String> findByPositionCdAndLevelCd(@Param("positionCodeList") List<String> positionCodeList, @Param("positionLevelCodeList") List<String> positionLevelCodeList);

	@Query("""
			select pr
			from ProjectRequirementEntity pr
			where (:projectGuids is null or pr.projectGuid in :projectGuids)
			""")
	List<ProjectRequirementEntity> findByProjectGuids(@Param("projectGuids") Set<String> projectGuids);

	@Query("""
			select pr
			from ProjectRequirementEntity pr
			where (:projectGuid is null or pr.projectGuid = :projectGuid)
			""")
	List<ProjectRequirementEntity> findByProjectGuid(@Param("projectGuid") String projectGuid);

	@Modifying
	@Query("""
			delete from ProjectRequirementEntity pr
			where pr.projectGuid = :projectGuid
			""")
	void deleteAllByProjectGuid(@Param("projectGuid") String projectGuid);
	Optional<ProjectRequirementEntity> findByProjectRequirementGuid(String projectRequirementGuid);
	
	
	Optional<ProjectRequirementEntity> findById(String projectRequirementGuid);
}
