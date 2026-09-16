package teamdevhub.devhub.administration.outbound.admin.form.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.administration.outbound.admin.form.adapter.entity.ApplicationFormItemEntity;

public interface JpaApplicationFormItemRepository extends JpaRepository<ApplicationFormItemEntity, String> {

	@Modifying
	@Query("""
			delete from ApplicationFormItemEntity afi
			where afi.formGuid in :applicationFormGuids
			""")
	void deleteAllByApplicationFormGuid(@Param("applicationFormGuids") List<String> applicationFormGuids);

	@Query("""
			select afi
			from ApplicationFormItemEntity afi
			where afi.formGuid = :applicationFormGuid
			""")
	List<ApplicationFormItemEntity> findByFormGuid(@Param("applicationFormGuid") String applicationFormGuid);

	@Query("""
			select afi
			from ApplicationFormItemEntity afi
			where afi.formGuid in :formGuids
			""")
	List<ApplicationFormItemEntity> findByFormGuidIn(@Param("formGuids") List<String> formGuids);

}
