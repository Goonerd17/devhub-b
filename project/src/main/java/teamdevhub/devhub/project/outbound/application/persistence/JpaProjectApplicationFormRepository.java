package teamdevhub.devhub.project.outbound.application.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.project.outbound.application.adapter.entity.ProjectApplicationFormEntity;

public interface JpaProjectApplicationFormRepository extends JpaRepository<ProjectApplicationFormEntity, String> {

	@Modifying
	@Query("""
			delete from ProjectApplicationFormEntity paf
			where paf.projectGuid = :projectGuid
			""")
	void deleteAllByProjectGuid(@Param("projectGuid") String projectGuid);

	@Query("""
			select paf.applicationFormGuid
			from ProjectApplicationFormEntity paf
			where paf.projectGuid = :projectGuid
			""")
	List<String> findAllGuidByProjectGuid(@Param("projectGuid") String projectGuid);

	List<ProjectApplicationFormEntity> findByProjectGuid(String projectGuid);

}
