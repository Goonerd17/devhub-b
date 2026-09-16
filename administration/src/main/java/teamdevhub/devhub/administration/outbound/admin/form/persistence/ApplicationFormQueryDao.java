package teamdevhub.devhub.administration.outbound.admin.form.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.administration.api.form.SearchApplicationFormCommand;

public interface ApplicationFormQueryDao {

	Page<ApplicationForm> listApplicationForm(SearchApplicationFormCommand searchApplicationFormCommand,
			Pageable pageable);

	Page<ApplicationForm> listApplicationFormWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand);

	List<ApplicationForm> listApplicationFormsWithItems(SearchApplicationFormCommand searchApplicationFormCommand);

}
