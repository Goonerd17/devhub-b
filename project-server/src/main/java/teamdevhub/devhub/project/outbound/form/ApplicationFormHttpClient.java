package teamdevhub.devhub.project.outbound.form;

import java.util.List;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.project.core.port.out.ApplicationFormPort;
import teamdevhub.devhub.shared.form.*;

@Component
public class ApplicationFormHttpClient implements ApplicationFormPort {
    private final ApplicationFormFeignClient client;

    public ApplicationFormHttpClient(ApplicationFormFeignClient client) {
        this.client = client;
    }

    @Override
    public ApplicationFormPage search(ApplicationFormSearch search, int page, int size) {
        return client.search(search, page, size);
    }

    @Override
    public List<String> create(List<ApplicationFormDefinition> forms) {
        return client.create(forms);
    }

    @Override
    public void deleteApplicationForms(List<String> formGuids) {
        client.deleteApplicationForms(formGuids);
    }

    @Override
    public List<ApplicationFormView> findStandard(List<String> formGuids) {
        return client.findStandard(formGuids);
    }

    @Override
    public List<ApplicationFormView> findCustomized(List<String> formGuids) {
        return client.findCustomized(formGuids);
    }
}
