package teamdevhub.devhub.administration.core.admin.form.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.administration.api.form.SearchApplicationFormCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;

public interface ApplicationFormQueryUseCase {

	PageResult<ApplicationForm> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand);

	List<ApplicationForm> getApplicationFormsWithItems(SearchApplicationFormCommand searchApplicationFormCommand);

}
