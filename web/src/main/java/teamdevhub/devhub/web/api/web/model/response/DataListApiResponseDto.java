package teamdevhub.devhub.web.api.web.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataListApiResponseDto<T> {

    @JsonProperty("success")
    private boolean isSuccess;
    private String code;
    private List<T> dataList;
    private PageResponseDto pagination;
    private ErrorResponseDto error;

    public static <T> DataListApiResponseDto<T> successWithDataList(SuccessCode successCode, List<T> dataList, PageResponseDto pageResponseDto) {
        return DataListApiResponseDto.<T>builder()
                .isSuccess(true)
                .code(successCode.getCode())
                .dataList(dataList)
                .pagination(pageResponseDto)
                .build();
    }

    public static <T> DataListApiResponseDto<T> successWithDataList(SuccessCode successCode, List<T> dataList) {
        return DataListApiResponseDto.<T>builder()
                .isSuccess(true)
                .code(successCode.getCode())
                .dataList(dataList)
                .pagination(null)
                .build();
    }
}