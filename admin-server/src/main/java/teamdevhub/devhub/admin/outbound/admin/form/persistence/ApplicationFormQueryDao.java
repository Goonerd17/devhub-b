package teamdevhub.devhub.admin.outbound.admin.form.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import teamdevhub.devhub.admin.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.admin.api.form.SearchApplicationFormCommand;

public interface ApplicationFormQueryDao {

	Page<ApplicationForm> listApplicationForm(SearchApplicationFormCommand searchApplicationFormCommand,
			Pageable pageable);

	Page<ApplicationForm> listApplicationFormWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand);

	List<ApplicationForm> listApplicationFormsWithItems(SearchApplicationFormCommand searchApplicationFormCommand);

}
