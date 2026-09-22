package teamdevhub.devhub.project.outbound.form;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.form.*;

@Component
public class ApplicationFormHttpClient implements ApplicationFormGateway {
    private final RestClient client;

    public ApplicationFormHttpClient(RestClient.Builder builder,
            @Value("${services.admin.base-url:http://admin-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @Override
    public ApplicationFormPage search(ApplicationFormSearch search, int page, int size) {
        return client.get().uri(uri -> uri.path("/internal/application-forms")
                .queryParamIfPresent("title", java.util.Optional.ofNullable(search.title()))
                .queryParamIfPresent("used", java.util.Optional.ofNullable(search.used()))
                .queryParamIfPresent("customized", java.util.Optional.ofNullable(search.customized()))
                .queryParam("page", page).queryParam("size", size).build())
                .retrieve().body(ApplicationFormPage.class);
    }

    @Override
    public List<String> create(List<ApplicationFormDefinition> forms) {
        List<String> result = client.post().uri("/internal/application-forms").body(forms).retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return result == null ? List.of() : result;
    }

    @Override
    public void deleteApplicationForms(List<String> formGuids) {
        client.method(org.springframework.http.HttpMethod.DELETE).uri("/internal/application-forms")
                .body(formGuids).retrieve().toBodilessEntity();
    }

    @Override
    public List<ApplicationFormView> findStandard(List<String> formGuids) {
        return query("/internal/application-forms/standard/query", formGuids);
    }

    @Override
    public List<ApplicationFormView> findCustomized(List<String> formGuids) {
        return query("/internal/application-forms/customized/query", formGuids);
    }

    private List<ApplicationFormView> query(String path, List<String> formGuids) {
        List<ApplicationFormView> result = client.post().uri(path).body(formGuids).retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return result == null ? List.of() : result;
    }
}
