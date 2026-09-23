package teamdevhub.devhub.auth.outbound.terms;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.shared.terms.AgreeTermsCommand;
import teamdevhub.devhub.auth.core.port.out.TermsAgreementPort;

@Component
public class TermsAgreementHttpClient implements TermsAgreementPort {
    private final TermsAgreementFeignClient client;

    public TermsAgreementHttpClient(TermsAgreementFeignClient client) {
        this.client = client;
    }

    @Override
    public void save(AgreeTermsCommand command) {
        client.save(command);
    }
}
