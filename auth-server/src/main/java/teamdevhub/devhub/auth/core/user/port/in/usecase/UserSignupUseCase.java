package teamdevhub.devhub.auth.core.user.port.in.usecase;

import teamdevhub.devhub.auth.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupUserCommand;

public interface UserSignupUseCase {

    void initializeAdminUser(SignupAdminCommand signupAdminCommand);
    void saveEmailUserInfo(SignupUserCommand signupUserCommand, String userGuid);
    void saveOAuthUserInfo(SignupOAuthUserCommand signupOAuthUserCommand, String userGuid);
}
