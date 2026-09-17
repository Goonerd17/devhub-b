package teamdevhub.devhub.admin.core.admin.code.port.out;

import teamdevhub.devhub.admin.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.admin.core.admin.code.domain.CommonCodeDetail;

import java.util.List;
import java.util.Map;

public interface CommonCodeRepository {
    List<CommonCode> getCommonCodeList();
    List<CommonCodeDetail> getCommonCodeDetailList();
    void saveAll(List<CommonCode> commonCodes);
    void save(CommonCode commonCode);
    boolean existsByCode(String code);

    default Map<String, String> findNames(List<String> codes) { return Map.of(); }
}
