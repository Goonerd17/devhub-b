package teamdevhub.devhub.admin.outbound.admin.form.adapter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.admin.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.admin.core.admin.form.port.out.ApplicationFormQueryRepository;
import teamdevhub.devhub.admin.api.form.SearchApplicationFormCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.admin.outbound.admin.form.persistence.ApplicationFormQueryDao;

@Component
@RequiredArgsConstructor
public class ApplicationFormQueryAdapter implements ApplicationFormQueryRepository {

	private final ApplicationFormQueryDao applicationFormQueryDao;

	@Override
	public PageResult<ApplicationForm> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand) {
		Page<ApplicationForm> page = applicationFormQueryDao.listApplicationFormWithoutItem(searchApplicationFormCommand);
		List<ApplicationForm> applicationFormList = page.getContent();
		return PageResult.of(
				applicationFormList,
				page.getNumber(),
				page.getSize(),
				page.getTotalElements()
		);
	}

	@Override
	public List<ApplicationForm> getApplicationFormsWithItems(SearchApplicationFormCommand searchApplicationFormCommand) {
		return applicationFormQueryDao.listApplicationFormsWithItems(searchApplicationFormCommand);
	}
}
