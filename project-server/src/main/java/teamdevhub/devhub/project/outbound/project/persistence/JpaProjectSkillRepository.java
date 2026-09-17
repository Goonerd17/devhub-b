package teamdevhub.devhub.project.outbound.project.persistence;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectSkillEntity;

public interface JpaProjectSkillRepository extends JpaRepository<ProjectSkillEntity, String> {

	@Query("""
			select ps.projectGuid
			from ProjectSkillEntity ps
			where (:skillCodeList is null or ps.skillCd in :skillCodeList)
			""")
	List<String> findBySkillCdList(@Param("skillCodeList")List<String> skillCodeList);

	@Query("""
			select ps
			from ProjectSkillEntity ps
			where (:projectGuids is null or ps.projectGuid in :projectGuids)
			""")
	List<ProjectSkillEntity> findByProjectGuids(@Param("projectGuids")Set<String> projectGuids);

	@Query("""
			select ps
			from ProjectSkillEntity ps
			where (:projectGuid is null or ps.projectGuid = :projectGuid)
			""")
	List<ProjectSkillEntity> findByProjectGuid(@Param("projectGuid") String projectGuid);

	@Modifying
	@Query("""
			delete from ProjectSkillEntity ps
			where ps.projectGuid = :projectGuid
			""")
	void deleteAllByProjectGuid(@Param("projectGuid") String projectGuid);

}
