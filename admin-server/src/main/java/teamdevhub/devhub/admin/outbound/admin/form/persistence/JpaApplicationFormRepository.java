package teamdevhub.devhub.admin.outbound.admin.form.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.admin.outbound.admin.form.adapter.entity.ApplicationFormEntity;

public interface JpaApplicationFormRepository  extends JpaRepository<ApplicationFormEntity, String>{

	Optional<ApplicationFormEntity> findByApplicationFormGuid(String applicationFormGuid);

	@Query("""
			select af
			from ApplicationFormEntity af
			where af.isCustomized = true
			and (:deleteApplicationFormGuids is null or af.applicationFormGuid in :deleteApplicationFormGuids)
			""")
	List<ApplicationFormEntity> findByIdAndIsCustomized(@Param("deleteApplicationFormGuids") List<String> deleteApplicationFormGuids);

	@Query("""
			select af
			from ApplicationFormEntity af
			where af.isCustomized = false
			and (:formList is null or af.applicationFormGuid in :formList)
			""")
	List<ApplicationFormEntity> findByIdAndIsNotCustomized(@Param("formList")List<String> formList);
}
