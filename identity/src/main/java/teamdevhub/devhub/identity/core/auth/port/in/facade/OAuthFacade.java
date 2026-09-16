package teamdevhub.devhub.identity.core.auth.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.identity.core.auth.application.service.AuthResult;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthResult;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthAuthorizationResult;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthUserResult;
import teamdevhub.devhub.member.api.MemberLoginActivityUseCase;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.identity.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.identity.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.identity.core.auth.port.in.usecase.oauth.OAuthAuthenticationUseCase;
import teamdevhub.devhub.identity.core.auth.port.in.usecase.oauth.OAuthResolveUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthFacade {

    private final OAuthAuthenticationUseCase oauthAuthenticationUseCase;
    private final OAuthResolveUseCase oauthResolveUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final MemberLoginActivityUseCase memberLoginActivityUseCase;

    public OAuthAuthorizationResult createOAuthAuthorizationUrl(String provider) {
        return oauthAuthenticationUseCase.createAuthorizationUrl(provider);
    }

    public OAuthResult handleOAuthCallback(String provider, String code) {
        OAuthUser oauthUser = oauthAuthenticationUseCase.handleOAuthCallback(VerificationProvider.from(provider), code);
        OAuthUserResult oauthUserResult = oauthResolveUseCase.findOrRequireSignup(oauthUser);

        if (oauthUserResult.loginAvailable()) {
            memberLoginActivityUseCase.assertMemberCanLogIn(oauthUserResult.authenticatedUser().userGuid());
            AuthResult authResult = authenticationUseCase.login(oauthUserResult.authenticatedUser());
            memberLoginActivityUseCase.recordSuccessfulLogin(oauthUserResult.authenticatedUser().userGuid());
            return OAuthResult.loggedIn(authResult);
        }

        String tempToken = oauthAuthenticationUseCase.issueTempToken(oauthUser);
        return OAuthResult.requiresSignup(tempToken);
    }
}
