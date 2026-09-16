package teamdevhub.devhub.identity.core.auth.port.in.usecase;

import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.identity.core.auth.port.in.command.LoginCommand;

public interface AuthenticatedUserUseCase {

    AuthenticatedUser getUserForReissue(String userGuid);
    AuthenticatedUser authenticate(LoginCommand loginCommand);
}
