package teamdevhub.devhub.auth.core.auth.port.in.usecase;

import teamdevhub.devhub.auth.core.auth.application.service.AuthResult;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;

public interface AuthenticationUseCase {

    AuthResult login(AuthenticatedUser authenticatedUser);
    AuthResult reissueAccessToken(AuthenticatedUser authenticatedUser);
    void revoke(String userGuid);
}
