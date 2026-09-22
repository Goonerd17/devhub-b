package teamdevhub.devhub.project.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.web.enums.SuccessCode;
import java.util.List;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class DataListApiResponseDto<T> {
    private boolean success;
    private String code;
    private List<T> dataList;
    private Object pagination;
    public static <T> DataListApiResponseDto<T> successWithDataList(SuccessCode code, List<T> data, Object pagination) {
        return DataListApiResponseDto.<T>builder().success(true).code(code.getCode()).dataList(data).pagination(pagination).build();
    }
    public static <T> DataListApiResponseDto<T> successWithDataList(SuccessCode code, List<T> data) { return successWithDataList(code, data, null); }
}

