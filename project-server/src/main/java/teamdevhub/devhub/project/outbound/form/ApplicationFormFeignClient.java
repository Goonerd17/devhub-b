package teamdevhub.devhub.project.outbound.form;

import java.util.List;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.project.core.port.out.ApplicationFormPort;
import teamdevhub.devhub.shared.form.*;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;

@FeignClient(name = "admin-server", configuration = InternalFeignConfiguration.class)
public interface ApplicationFormFeignClient extends ApplicationFormPort {
    @GetMapping("/internal/application-forms")
    ApplicationFormPage searchRaw(@RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean used, @RequestParam(required = false) Boolean customized,
            @RequestParam int page, @RequestParam int size);
    @Override default ApplicationFormPage search(ApplicationFormSearch search, int page, int size) {
        return searchRaw(search.title(), search.used(), search.customized(), page, size);
    }
    @Override @PostMapping("/internal/application-forms")
    List<String> create(@RequestBody List<ApplicationFormDefinition> forms);
    @Override @DeleteMapping("/internal/application-forms")
    void deleteApplicationForms(@RequestBody List<String> formGuids);
    @Override @PostMapping("/internal/application-forms/standard/query")
    List<ApplicationFormView> findStandard(@RequestBody List<String> formGuids);
    @Override @PostMapping("/internal/application-forms/customized/query")
    List<ApplicationFormView> findCustomized(@RequestBody List<String> formGuids);
}
