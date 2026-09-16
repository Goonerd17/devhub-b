package teamdevhub.devhub.identity.core.auth.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.identity.core.auth.application.selector.oauth.OAuthClientSelector;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthAuthorizationResult;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.identity.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.identity.core.auth.port.in.usecase.oauth.OAuthAuthenticationUseCase;
import teamdevhub.devhub.identity.core.auth.port.out.oauth.OAuthClient;
import teamdevhub.devhub.identity.core.auth.port.out.token.TokenIssueProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthAuthenticationService implements OAuthAuthenticationUseCase {

    private final TokenIssueProvider tokenIssueProvider;
    private final OAuthClientSelector oauthClientSelector;
    private final IdentifierProvider identifierProvider;

    @Override
    public OAuthAuthorizationResult createAuthorizationUrl(String provider) {
        VerificationProvider verificationProvider = VerificationProvider.from(provider);
        OAuthClient oauthClient = oauthClientSelector.select(verificationProvider);
        String state = identifierProvider.generateIdentifier();
        String url = oauthClient.getAuthorizationUrl(state);
        return OAuthAuthorizationResult.of(url, state);
    }

    @Override
    public OAuthUser handleOAuthCallback(VerificationProvider verificationProvider, String authorizationCode) {
        OAuthClient oauthClient = oauthClientSelector.select(verificationProvider);
        return oauthClient.fetchUser(authorizationCode);
    }

    @Override
    public String issueTempToken(OAuthUser oauthUser) {
        return tokenIssueProvider.createTempToken(oauthUser.oauthId(), oauthUser.verificationProvider(), oauthUser.email());
    }
}
