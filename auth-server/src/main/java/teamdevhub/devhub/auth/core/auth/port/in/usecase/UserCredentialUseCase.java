package teamdevhub.devhub.auth.core.auth.port.in.usecase;

import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.auth.api.credential.EmailCredentialRegistrationCommand;
import teamdevhub.devhub.auth.api.credential.UpdatePasswordCommand;
import teamdevhub.devhub.auth.core.auth.domain.vo.oauth.OAuthUser;

public interface UserCredentialUseCase {

    String signupEmailUser(EmailCredentialRegistrationCommand command);
    AuthenticatedUser signupOAuthUser(OAuthUser oauthUser);
    AuthenticatedUser getUserForReissue(String refreshToken);
    AuthenticatedUser authenticate(LoginCommand loginCommand);
    void updatePassword(UpdatePasswordCommand updatePasswordCommand);
    void resetUserPassword(String userGuid, String newPassword);
}
