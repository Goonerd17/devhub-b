package teamdevhub.devhub.project.outbound.project.persistence;


import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectLikeEntity;

public interface JpaProjectLikeRepository extends JpaRepository<ProjectLikeEntity, String> {

	@Query("""
			select pl
			from ProjectLikeEntity pl
			where (:projectGuids is null or pl.projectGuid in :projectGuids)
			""")
	List<ProjectLikeEntity> findByProjectGuids(@Param("projectGuids")Set<String> projectGuids);

	@Query("""
			select count(pl)
			from ProjectLikeEntity pl
			where (:projectGuid is null or pl.projectGuid = :projectGuid)
			""")
	int countByProjectGuid(@Param("projectGuid") String projectGuid);

	ProjectLikeEntity findByProjectGuidAndUserGuid(String projectGuid, String userGuid);
	
	@Modifying
	@Query("""
			delete from ProjectLikeEntity pl
			where pl.projectGuid = :projectGuid
			""")
	void deleteAllByProjectGuid(@Param("projectGuid")String projectGuid);

	@Query("""
			select pl
			from ProjectLikeEntity pl
			where pl.userGuid = :userGuid
			""")
	Page<ProjectLikeEntity> findByUserGuid(@Param("userGuid")String userGuid, Pageable pageable);
}
