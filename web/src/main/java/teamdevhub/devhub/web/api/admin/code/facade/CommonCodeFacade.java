package teamdevhub.devhub.web.api.admin.code.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.web.api.admin.code.model.response.CommonCodeResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.administration.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.administration.core.admin.code.port.in.command.CommonCodeCommand;
import teamdevhub.devhub.administration.core.admin.code.port.in.usecase.CommonCodeUseCase;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCodeFacade {

    private final CommonCodeUseCase commonCodeUseCase;

    public DataListApiResponseDto<CommonCodeResponseDto> getCommonCodeList() {
        List<CommonCodeResponseDto> returnData = commonCodeUseCase.getCommonCodeList().stream()
                .map(CommonCodeResponseDto::fromDomain)
                .toList();

        return DataListApiResponseDto.successWithDataList(
                SuccessCode.READ_SUCCESS,
                returnData,
                null
        );
    }

    public DataApiResponseDto<?> saveCode(CommonCodeCommand code, boolean isInsert) {
        if (isInsert) {
            if (commonCodeUseCase.isDuplicate(code.code())) {
                return DataApiResponseDto.failureWithMessage(ErrorCode.CREATE_FAIL, "이미 존재하는 코드입니다: " + code.code());
            }
        }
        commonCodeUseCase.saveCommonCode(CommonCode.createCommonCode(code));
        return DataApiResponseDto.successWithData(
                SuccessCode.READ_SUCCESS,
                null
        );
    }
}
