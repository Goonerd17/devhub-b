package teamdevhub.devhub.identity.core.auth.port.in.usecase.oauth;

import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthUserResult;
import teamdevhub.devhub.identity.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.identity.core.auth.port.in.command.oauth.SignupOAuthUserCommand;

public interface OAuthResolveUseCase {

    OAuthUserResult findOrRequireSignup(OAuthUser oauthUser);
    OAuthUser extractOAuthUser(SignupOAuthUserCommand signupOAuthUserCommand);
}
