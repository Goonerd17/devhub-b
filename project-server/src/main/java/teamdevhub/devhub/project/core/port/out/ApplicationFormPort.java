package teamdevhub.devhub.project.core.port.out;
import java.util.List;
import teamdevhub.devhub.shared.form.*;
public interface ApplicationFormPort {
    ApplicationFormPage search(ApplicationFormSearch search, int page, int size);
    List<String> create(List<ApplicationFormDefinition> forms);
    void deleteApplicationForms(List<String> formGuids);
    List<ApplicationFormView> findStandard(List<String> formGuids);
    List<ApplicationFormView> findCustomized(List<String> formGuids);
}
