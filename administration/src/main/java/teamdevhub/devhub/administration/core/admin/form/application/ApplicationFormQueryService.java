package teamdevhub.devhub.administration.core.admin.form.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.administration.core.admin.form.port.in.usecase.ApplicationFormQueryUseCase;
import teamdevhub.devhub.administration.core.admin.form.port.out.ApplicationFormQueryRepository;
import teamdevhub.devhub.administration.api.form.SearchApplicationFormCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationFormQueryService implements ApplicationFormQueryUseCase {

	private final ApplicationFormQueryRepository applicationFormQueryRepository;

	@Override
	public PageResult<ApplicationForm> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand) {
		return applicationFormQueryRepository.getApplicationFormsWithoutItem(searchApplicationFormCommand);
	}

	@Override
	public List<ApplicationForm> getApplicationFormsWithItems(SearchApplicationFormCommand searchApplicationFormCommand) {
		return applicationFormQueryRepository.getApplicationFormsWithItems(searchApplicationFormCommand);
	}

}
