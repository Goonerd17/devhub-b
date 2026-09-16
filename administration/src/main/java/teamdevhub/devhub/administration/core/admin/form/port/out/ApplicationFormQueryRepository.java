package teamdevhub.devhub.administration.core.admin.form.port.out;

import java.util.List;

import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.administration.api.form.SearchApplicationFormCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;

public interface ApplicationFormQueryRepository {

	PageResult<ApplicationForm> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand);

	List<ApplicationForm> getApplicationFormsWithItems(SearchApplicationFormCommand searchApplicationFormCommand);

}
