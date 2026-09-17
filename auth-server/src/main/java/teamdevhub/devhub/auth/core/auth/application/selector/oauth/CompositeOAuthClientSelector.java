package teamdevhub.devhub.auth.core.auth.application.selector.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.auth.core.auth.port.out.oauth.OAuthClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeOAuthClientSelector implements OAuthClientSelector {

    private final List<OAuthClient> oauthClientList;

    @Override
    public OAuthClient select(VerificationProvider verificationProvider) {
        return oauthClientList.stream()
                .filter(oauthClient -> oauthClient.supports(verificationProvider))
                .findFirst()
                .orElseThrow(() -> BusinessRuleException.of(ErrorCode.OAUTH_FAIL));
    }
}
