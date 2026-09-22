package teamdevhub.devhub.auth.outbound.terms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.terms.AgreeTermsCommand;
import teamdevhub.devhub.shared.terms.TermsAgreementGateway;

@Component
public class TermsAgreementHttpClient implements TermsAgreementGateway {
    private final RestClient client;

    public TermsAgreementHttpClient(RestClient.Builder builder,
            @Value("${services.admin.base-url:http://admin-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @Override
    public void save(AgreeTermsCommand command) {
        client.post().uri("/internal/terms/agreements").body(command).retrieve().toBodilessEntity();
    }
}
