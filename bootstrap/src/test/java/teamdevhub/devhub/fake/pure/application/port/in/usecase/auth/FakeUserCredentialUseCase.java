package teamdevhub.devhub.fake.pure.application.port.in.usecase.auth;

import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.identity.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.identity.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.identity.api.credential.EmailCredentialRegistrationCommand;
import teamdevhub.devhub.identity.api.credential.UpdatePasswordCommand;
import teamdevhub.devhub.identity.core.auth.domain.vo.oauth.OAuthUser;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserCredentialUseCase implements UserCredentialUseCase {

    @Override
    public String signupEmailUser(EmailCredentialRegistrationCommand command) {
        return TEMP_TOKEN;
    }

    @Override
    public AuthenticatedUser signupOAuthUser(OAuthUser oauthUser) {
        return AuthenticatedUser.builder()
                .userGuid(TEST_USER_GUID_1)
                .build();
    }

    @Override
    public AuthenticatedUser getUserForReissue(String refreshToken) {
        return AuthenticatedUser.builder()
                .userGuid(TEST_USER_GUID_1)
                .build();
    }

    @Override
    public AuthenticatedUser authenticate(LoginCommand loginCommand) {
        return AuthenticatedUser.builder()
                .userGuid(TEST_USER_GUID_1)
                .loginId(loginCommand.email())
                .build();
    }

    @Override
    public void updatePassword(UpdatePasswordCommand updatePasswordCommand) {
    }

    @Override
    public void resetUserPassword(String userGuid, String newPassword) {
    }
}
