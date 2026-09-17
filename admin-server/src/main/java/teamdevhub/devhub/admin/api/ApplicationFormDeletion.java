package teamdevhub.devhub.admin.api;

import java.util.List;

/** Project가 프로젝트 삭제 후 사용자 정의 지원 폼을 정리하기 위한 공개 계약. */
public interface ApplicationFormDeletion {
    void deleteApplicationForms(List<String> applicationFormGuids);
}
