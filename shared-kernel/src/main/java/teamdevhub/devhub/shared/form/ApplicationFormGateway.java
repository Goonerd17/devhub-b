package teamdevhub.devhub.shared.form;

import java.util.List;

public interface ApplicationFormGateway {
    ApplicationFormPage search(ApplicationFormSearch search, int page, int size);
    List<String> create(List<ApplicationFormDefinition> forms);
    void deleteApplicationForms(List<String> formGuids);
    List<ApplicationFormView> findStandard(List<String> formGuids);
    List<ApplicationFormView> findCustomized(List<String> formGuids);
}
