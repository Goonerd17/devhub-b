package teamdevhub.devhub.admin.core.admin.form.port.out;

import java.util.List;
import java.util.Set;

import teamdevhub.devhub.admin.core.admin.form.domain.ApplicationFormItem;

public interface ApplicationFormItemRepository {

	void saveAll(Set<ApplicationFormItem> items);

	void deleteByApplicationFormGuid(List<String> applicationFormGuids);

	List<ApplicationFormItem> findByFormGuid(String applicationFormGuid);

}
