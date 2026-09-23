package teamdevhub.devhub.auth.outbound.terms;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;
import teamdevhub.devhub.shared.terms.AgreeTermsCommand;
import teamdevhub.devhub.auth.core.port.out.TermsAgreementPort;

@FeignClient(name = "admin-server", configuration = InternalFeignConfiguration.class)
public interface TermsAgreementFeignClient extends TermsAgreementPort {
    @Override
    @PostMapping("/internal/terms/agreements")
    void save(@RequestBody AgreeTermsCommand command);
}
