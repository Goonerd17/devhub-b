package teamdevhub.devhub.auth.core.auth.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.auth.core.auth.application.service.oauth.vo.OAuthUserResult;
import teamdevhub.devhub.auth.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.auth.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.auth.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.auth.core.auth.port.in.usecase.oauth.OAuthResolveUseCase;
import teamdevhub.devhub.auth.core.auth.port.out.token.TokenParseProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthResolveService implements OAuthResolveUseCase {

    private final TokenParseProvider tokenParseProvider;
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public OAuthUserResult findOrRequireSignup(OAuthUser oauthUser) {

        return userCredentialRepository
                .findOAuthUserCredentialByOAuth(
                        oauthUser.verificationProvider(),
                        oauthUser.oauthId()
                )
                .map(OAuthUserResult::success)
                .orElseGet(OAuthUserResult::requiresSignup);
    }

    @Override
    public OAuthUser extractOAuthUser(SignupOAuthUserCommand signupOAuthUserCommand) {
        var tempTokenInfo = tokenParseProvider.getTempTokenInfo(signupOAuthUserCommand.tempToken());
        return new OAuthUser(tempTokenInfo.oauthId(), tempTokenInfo.verificationProvider(), tempTokenInfo.email());
    }
}
