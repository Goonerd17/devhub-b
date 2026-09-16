package teamdevhub.devhub.web.api.web.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.web.api.web.model.response.CommonCodeResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.administration.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.administration.core.admin.code.domain.CommonCodeDetail;
import teamdevhub.devhub.administration.core.admin.code.port.in.command.CommonCodeCommand;
import teamdevhub.devhub.administration.core.admin.code.port.in.usecase.CommonCodeUseCase;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonFacade {

	private final CommonCodeUseCase commonCodeUseCase;
	
	public DataApiResponseDto<Map<String, CommonCodeResponseDto>> getCommonCodeList() {

		List<CommonCodeDetail> commonCodeList = commonCodeUseCase.getCommonCodeDetailList();
		Map<String, CommonCodeResponseDto> result = commonCodeList.stream()
				.map(CommonCodeResponseDto::fromDetailDomain)
				.collect(Collectors.toMap(
						CommonCodeResponseDto::getCode,
						dto -> dto
				));

		return DataApiResponseDto.successWithData(
				SuccessCode.READ_SUCCESS,
				result
		);
	}

	public void saveCommonCode(CommonCodeCommand commonCodeCommand) {
		commonCodeUseCase.saveCommonCode(CommonCode.createCommonCode(commonCodeCommand));
	}

	public void saveCommonCodeList(List<CommonCodeCommand> commonCodeCommandList) {
		commonCodeUseCase.saveCommonCodeList(commonCodeCommandList.stream().map(CommonCode::createCommonCode).toList());
	}

}
