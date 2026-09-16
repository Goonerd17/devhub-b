package teamdevhub.devhub.identity.core.auth.port.in.usecase;

import teamdevhub.devhub.identity.core.auth.application.service.AuthResult;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;

public interface AuthenticationUseCase {

    AuthResult login(AuthenticatedUser authenticatedUser);
    AuthResult reissueAccessToken(AuthenticatedUser authenticatedUser);
    void revoke(String userGuid);
}
