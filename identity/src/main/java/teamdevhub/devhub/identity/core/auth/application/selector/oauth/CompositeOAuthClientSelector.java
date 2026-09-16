package teamdevhub.devhub.identity.core.auth.application.selector.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.identity.core.auth.port.out.oauth.OAuthClient;

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
