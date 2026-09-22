package teamdevhub.devhub.admin.http.internal;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.admin.api.*;
import teamdevhub.devhub.shared.form.ApplicationFormDefinition;
import teamdevhub.devhub.shared.form.ApplicationFormPage;
import teamdevhub.devhub.shared.form.ApplicationFormSearch;
import teamdevhub.devhub.shared.form.ApplicationFormView;

@RestController
@RequestMapping("/internal/application-forms")
@RequiredArgsConstructor
public class ApplicationFormInternalController {
    private final ApplicationFormCatalogQuery catalogQuery;
    private final ApplicationFormCreation creation;
    private final ApplicationFormDeletion deletion;
    private final ApplicationFormQuery query;

    @GetMapping
    public ApplicationFormPage search(@RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean used,
            @RequestParam(required = false) Boolean customized,
            @RequestParam int page, @RequestParam int size) {
        var result = catalogQuery.search(new teamdevhub.devhub.admin.api.ApplicationFormSearch(title, used, customized), page, size);
        return new ApplicationFormPage(result.content().stream().map(this::view).toList(), result.page(), result.size(),
                result.totalElements(), result.totalPages(), result.first(), result.last());
    }

    @PostMapping
    public List<String> create(@RequestBody List<ApplicationFormDefinition> forms) {
        return creation.create(forms.stream().map(form -> new teamdevhub.devhub.admin.api.ApplicationFormDefinition(
                form.typeCd(), form.title(), form.helpText(), form.itemList())).toList());
    }

    @DeleteMapping
    public void delete(@RequestBody List<String> formGuids) { deletion.deleteApplicationForms(formGuids); }

    @PostMapping("/standard/query")
    public List<ApplicationFormView> standard(@RequestBody List<String> formGuids) {
        return query.findStandard(formGuids).stream().map(this::view).toList();
    }

    @PostMapping("/customized/query")
    public List<ApplicationFormView> customized(@RequestBody List<String> formGuids) {
        return query.findCustomized(formGuids).stream().map(this::view).toList();
    }

    private ApplicationFormView view(teamdevhub.devhub.admin.api.ApplicationFormView form) {
        return new ApplicationFormView(form.applicationFormGuid(), form.typeCd(), form.title(), form.helpText(),
                form.customized(), form.used(), form.itemList());
    }
}
