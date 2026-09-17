package teamdevhub.devhub.admin.core.admin.code.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.admin.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.admin.core.admin.code.domain.CommonCodeDetail;
import teamdevhub.devhub.admin.core.admin.code.port.in.usecase.CommonCodeUseCase;
import teamdevhub.devhub.admin.core.admin.code.port.out.CommonCodeRepository;
import teamdevhub.devhub.admin.api.CommonCodeNameQuery;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonCodeService implements CommonCodeUseCase, CommonCodeNameQuery {

    private final CommonCodeRepository commonCodeRepository;

    @Override
    public List<CommonCodeDetail> getCommonCodeDetailList() {
        return commonCodeRepository.getCommonCodeDetailList();
    }

    @Override
    public List<CommonCode> getCommonCodeList() {
        return commonCodeRepository.getCommonCodeList();
    }

    @Override
    public void saveCommonCode(CommonCode commonCode) {
        commonCodeRepository.save(commonCode);
    }

    @Override
    public void saveCommonCodeList(List<CommonCode> commonCodeList) {
        commonCodeRepository.saveAll(commonCodeList);
    }

    @Override
    public boolean isDuplicate(String code) {
        return commonCodeRepository.existsByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.Map<String, String> findNames(List<String> codes) {
        return commonCodeRepository.findNames(codes);
    }
}
