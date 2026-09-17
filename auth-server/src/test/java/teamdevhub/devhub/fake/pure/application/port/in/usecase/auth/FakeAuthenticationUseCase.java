package teamdevhub.devhub.fake.pure.application.port.in.usecase.auth;

import teamdevhub.devhub.auth.core.auth.application.service.AuthResult;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.port.in.usecase.AuthenticationUseCase;

public class FakeAuthenticationUseCase implements AuthenticationUseCase {

    private String revokedUserGuid;
    private AuthenticatedUser lastLoginUser;

    @Override
    public AuthResult login(AuthenticatedUser authenticatedUser) {
        this.lastLoginUser = authenticatedUser;
        return AuthResult.of("access-token", "refresh-token");
    }

    @Override
    public AuthResult reissueAccessToken(AuthenticatedUser authenticatedUser) {
        return AuthResult.of("new-access-token", "new-refresh-token");
    }

    @Override
    public void revoke(String userGuid) {
        this.revokedUserGuid = userGuid;
    }

    public String getRevokedUserGuid() {
        return revokedUserGuid;
    }

    public AuthenticatedUser getLastLoginUser() {
        return lastLoginUser;
    }
}
