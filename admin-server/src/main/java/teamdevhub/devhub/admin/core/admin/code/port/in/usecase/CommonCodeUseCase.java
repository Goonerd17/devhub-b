package teamdevhub.devhub.admin.core.admin.code.port.in.usecase;

import teamdevhub.devhub.admin.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.admin.core.admin.code.domain.CommonCodeDetail;

import java.util.List;

public interface CommonCodeUseCase {
    List<CommonCodeDetail> getCommonCodeDetailList();
    List<CommonCode> getCommonCodeList();
    void saveCommonCode(CommonCode commonCode);
    void saveCommonCodeList(List<CommonCode> commonCodeList);
    boolean isDuplicate(String code);
}
