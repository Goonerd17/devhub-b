package teamdevhub.devhub.admin.api;

import java.util.List;

public interface ApplicationFormQuery {
    List<ApplicationFormView> findStandard(List<String> formGuids);
    List<ApplicationFormView> findCustomized(List<String> formGuids);
}
