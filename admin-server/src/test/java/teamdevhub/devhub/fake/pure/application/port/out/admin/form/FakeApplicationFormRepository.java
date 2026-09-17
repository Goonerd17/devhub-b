package teamdevhub.devhub.fake.pure.application.port.out.admin.form;

import java.util.ArrayList;
import java.util.List;

import teamdevhub.devhub.admin.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.admin.core.admin.form.port.out.ApplicationFormRepository;

public class FakeApplicationFormRepository implements ApplicationFormRepository {
	
	private final List<ApplicationForm> store = new ArrayList<>();
	
	public ApplicationForm findByApplicationFormGuid(String applicationFormGuid) {
		return store.stream()
				.filter(form -> applicationFormGuid.equals(form.getApplicationFormGuid()))
				.findAny()
				.get();
	}

	@Override
	public void deleteByApplicationFormGuid(List<String> applicationFormGuids) {

	}

	@Override
	public List<ApplicationForm> findByIdAndIsCustomized(List<String> deleteApplicationFormGuids) {
		return List.of();
	}

	@Override
	public List<ApplicationForm> findByIdAndIsNotCustomized(List<String> formList) {
		return List.of();
	}

	@Override
	public void save(ApplicationForm applicationForm) {
		store.add(applicationForm);
	}

	@Override
	public void update(ApplicationForm applicationForm) {
		store.removeIf(form -> applicationForm.getApplicationFormGuid().equals(form.getApplicationFormGuid()));
		store.add(applicationForm);
	}

}
