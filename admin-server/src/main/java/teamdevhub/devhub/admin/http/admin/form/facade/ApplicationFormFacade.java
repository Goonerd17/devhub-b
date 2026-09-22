package teamdevhub.devhub.admin.http.admin.form.facade;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.admin.http.response.DataListApiResponseDto;
import teamdevhub.devhub.admin.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.admin.core.admin.form.port.in.command.SaveApplicationFormCommand;
import teamdevhub.devhub.admin.core.admin.form.port.in.usecase.ApplicationFormQueryUseCase;
import teamdevhub.devhub.admin.core.admin.form.port.in.usecase.ApplicationFormUseCase;
import teamdevhub.devhub.admin.api.form.SearchApplicationFormCommand;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

@Service("adminApplicationFormFacade")
@Transactional
@RequiredArgsConstructor
public class ApplicationFormFacade {

	private final ApplicationFormUseCase applicationFormUseCase;
	private final ApplicationFormQueryUseCase applicationFormQueryUseCase;

	public DataListApiResponseDto<ApplicationFormListItemDto> getApplicationForms(
			SearchApplicationFormCommand command) {
		List<ApplicationForm> forms = applicationFormQueryUseCase.getApplicationFormsWithItems(command);
		List<ApplicationFormListItemDto> dataList = IntStream.range(0, forms.size())
				.mapToObj(i -> ApplicationFormListItemDto.fromDomain(forms.get(i), i + 1))
				.toList();
		return DataListApiResponseDto.successWithDataList(SuccessCode.READ_SUCCESS, dataList);
	}

	public DataApiResponseDto<Void> saveApplicationForm(SaveApplicationFormCommand command) {
		applicationFormUseCase.saveApplicationForm(command);
		return DataApiResponseDto.successWithoutData(command.isInsert() ? SuccessCode.CREATE_SUCCESS : SuccessCode.UPDATE_SUCCESS);
	}

	public DataApiResponseDto<Void> deleteApplicationForm(String applicationFormGuid) {
		List<String> applicationFormGuidList = List.of(applicationFormGuid);
		applicationFormUseCase.deleteApplicationForms(applicationFormGuidList);
		return DataApiResponseDto.successWithoutData(SuccessCode.DELETE_SUCCESS);
	}

	public record ApplicationFormListItemDto(
			String applicationFormGuid,
			Integer classify,
			String fieldName,
			String type,
			List<String> options,
			String helpYn,
			String helpText,
			String usedYn,
			String defaultFieldYn
	) {
		public static ApplicationFormListItemDto fromDomain(ApplicationForm form, int classify) {
			return new ApplicationFormListItemDto(
					form.getApplicationFormGuid(),
					classify,
					form.getTitle(),
					form.getTypeCd(),
					form.getItems(),
					(form.getHelpText() != null && !form.getHelpText().isBlank()) ? "true" : "false",
					form.getHelpText(),
					form.isUsed() ? "Y" : "N",
					form.isCustomized() ? "N" : "Y"
			);
		}
	}
}

