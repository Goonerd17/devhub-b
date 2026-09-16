package teamdevhub.devhub.administration.core.admin.form.port.out;


import java.util.List;

import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationForm;

public interface ApplicationFormRepository {

	void save(ApplicationForm applicationForm);

	void update(ApplicationForm applicationForm);

	ApplicationForm findByApplicationFormGuid(String applicationFormGuid);

	void deleteByApplicationFormGuid(List<String> applicationFormGuids);

	List<ApplicationForm> findByIdAndIsCustomized(List<String> deleteApplicationFormGuids);

	List<ApplicationForm> findByIdAndIsNotCustomized(List<String> formList);

}
