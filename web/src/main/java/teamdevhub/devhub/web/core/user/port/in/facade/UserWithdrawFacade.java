package teamdevhub.devhub.web.core.user.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.identity.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserWithdrawUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWithdrawFacade {

    private final UserWithdrawUseCase userWithdrawUseCase;
    private final AuthenticationUseCase authenticationUseCase;

    public void withdraw(String userGuid) {
        userWithdrawUseCase.withdraw(userGuid);
        authenticationUseCase.revoke(userGuid);
    }
}
