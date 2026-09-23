package teamdevhub.devhub.project.http.application.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.project.http.response.DataListApiResponseDto;
import teamdevhub.devhub.project.http.response.PageResponseDto;
import teamdevhub.devhub.project.core.port.out.ApplicationFormPort;
import teamdevhub.devhub.shared.form.ApplicationFormPage;
import teamdevhub.devhub.shared.form.ApplicationFormSearch;
import teamdevhub.devhub.project.http.application.model.ApplicationFormResponseDto;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.web.enums.SuccessCode;

@Service("applicationApplicationFormFacade")
@RequiredArgsConstructor
public class ApplicationFormFacade {
	
	private final ApplicationFormPort applicationFormCatalogQuery;
	
	public DataListApiResponseDto<ApplicationFormResponseDto> getApplicationFormsWithoutItem(ApplicationFormSearch searchApplicationFormCommand) {
		ApplicationFormPage pagedApplicationList = applicationFormCatalogQuery.search(
				searchApplicationFormCommand,
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
