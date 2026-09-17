package teamdevhub.devhub.admin.core.admin.form.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.admin.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.admin.api.form.SearchApplicationFormCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;

public interface ApplicationFormQueryUseCase {

	PageResult<ApplicationForm> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand);

	List<ApplicationForm> getApplicationFormsWithItems(SearchApplicationFormCommand searchApplicationFormCommand);

}
