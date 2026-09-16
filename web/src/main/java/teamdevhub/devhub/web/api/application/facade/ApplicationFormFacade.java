package teamdevhub.devhub.web.api.application.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.administration.api.ApplicationFormCatalogQuery;
import teamdevhub.devhub.administration.api.ApplicationFormPage;
import teamdevhub.devhub.administration.api.ApplicationFormSearch;
import teamdevhub.devhub.administration.api.form.SearchApplicationFormCommand;
import teamdevhub.devhub.web.core.application.port.in.facade.model.ApplicationFormResponseDto;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

@Service("applicationApplicationFormFacade")
@RequiredArgsConstructor
public class ApplicationFormFacade {
	
	private final ApplicationFormCatalogQuery applicationFormCatalogQuery;
	
	public DataListApiResponseDto<ApplicationFormResponseDto> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand) {
		ApplicationFormPage pagedApplicationList = applicationFormCatalogQuery.search(
				new ApplicationFormSearch(searchApplicationFormCommand.title(), searchApplicationFormCommand.isUsed(), searchApplicationFormCommand.isCustomized()),
				0, Integer.MAX_VALUE);
		List<ApplicationFormResponseDto> applicationFormResponseDtoList = pagedApplicationList.content().stream()
				.map(ApplicationFormResponseDto::fromView)
				.toList();
		
		return DataListApiResponseDto.successWithDataList(
				SuccessCode.READ_SUCCESS,
				applicationFormResponseDtoList,
				PageResponseDto.from(pagedApplicationList));
	}
}
