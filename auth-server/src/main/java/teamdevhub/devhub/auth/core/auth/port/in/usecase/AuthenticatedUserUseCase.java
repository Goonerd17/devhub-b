package teamdevhub.devhub.auth.core.auth.port.in.usecase;

import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.port.in.command.LoginCommand;

public interface AuthenticatedUserUseCase {

    AuthenticatedUser getUserForReissue(String userGuid);
    AuthenticatedUser authenticate(LoginCommand loginCommand);
}
