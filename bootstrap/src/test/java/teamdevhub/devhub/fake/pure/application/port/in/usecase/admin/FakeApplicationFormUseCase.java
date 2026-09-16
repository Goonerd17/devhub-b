package teamdevhub.devhub.fake.pure.application.port.in.usecase.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.administration.core.admin.form.port.in.command.ApplicationFormCommand;
import teamdevhub.devhub.administration.core.admin.form.port.in.command.CreateApplicationFormCommand;
import teamdevhub.devhub.administration.core.admin.form.port.in.command.SaveApplicationFormCommand;
import teamdevhub.devhub.administration.core.admin.form.port.in.usecase.ApplicationFormUseCase;

public class FakeApplicationFormUseCase implements ApplicationFormUseCase {

	private final Map<String, ApplicationForm> store = new HashMap<>();

	@Override
	public List<String> saveApplicationForms(List<CreateApplicationFormCommand> additionalFormList) {
		return List.of();
	}

	@Override
	public void saveApplicationForm(SaveApplicationFormCommand command) {
	}

	@Override
	public void deleteApplicationForms(List<String> deleteApplicationFormGuids) {
	}

	@Override
	public List<ApplicationForm> getNoCustomizedFormById(List<String> formList) {
		return List.of();
	}

	@Override
	public List<ApplicationFormCommand> getCustomizedFormById(List<String> formList) {
		return List.of();
	}

}
