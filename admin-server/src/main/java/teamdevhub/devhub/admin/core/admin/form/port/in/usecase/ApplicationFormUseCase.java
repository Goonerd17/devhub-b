package teamdevhub.devhub.admin.core.admin.form.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.admin.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.admin.core.admin.form.port.in.command.ApplicationFormCommand;
import teamdevhub.devhub.admin.core.admin.form.port.in.command.CreateApplicationFormCommand;
import teamdevhub.devhub.admin.core.admin.form.port.in.command.SaveApplicationFormCommand;

public interface ApplicationFormUseCase {

	List<String> saveApplicationForms(List<CreateApplicationFormCommand> additionalFormList);

	void saveApplicationForm(SaveApplicationFormCommand command);

	void deleteApplicationForms(List<String> deleteApplicationFormGuids);

	List<ApplicationForm> getNoCustomizedFormById(List<String> formList);

	List<ApplicationFormCommand> getCustomizedFormById(List<String> formList);

}
