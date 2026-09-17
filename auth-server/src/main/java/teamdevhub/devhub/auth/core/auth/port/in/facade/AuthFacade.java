package teamdevhub.devhub.auth.core.auth.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.auth.core.auth.application.service.AuthResult;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.auth.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.auth.api.credential.UpdatePasswordCommand;
import teamdevhub.devhub.auth.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.member.api.MemberLoginActivityUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthFacade {

    private final UserCredentialUseCase userCredentialUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final MemberLoginActivityUseCase memberLoginActivityUseCase;

    public AuthResult login(LoginCommand loginCommand) {
        AuthenticatedUser authenticatedUser = userCredentialUseCase.authenticate(loginCommand);
        memberLoginActivityUseCase.assertMemberCanLogIn(authenticatedUser.userGuid());
        AuthResult authResult = authenticationUseCase.login(authenticatedUser);
        memberLoginActivityUseCase.recordSuccessfulLogin(authenticatedUser.userGuid());
        return authResult;
    }

    public AuthResult reissueAccessToken(String token) {
        AuthenticatedUser authenticatedUser = userCredentialUseCase.getUserForReissue(token);
        return authenticationUseCase.reissueAccessToken(authenticatedUser);
    }

    public void logout(String userGuid) {
        authenticationUseCase.revoke(userGuid);
    }

    public void updatePassword(UpdatePasswordCommand updatePasswordCommand) {
        userCredentialUseCase.updatePassword(updatePasswordCommand);
    }
}
