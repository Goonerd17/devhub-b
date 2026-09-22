package teamdevhub.devhub.auth.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.auth.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.auth.core.user.port.in.usecase.UserSignupUseCase;
import teamdevhub.devhub.shared.member.AuthMemberGateway;
import teamdevhub.devhub.shared.member.AuthMemberRegistration;
import teamdevhub.devhub.shared.security.MemberRole;
import teamdevhub.devhub.shared.identifier.IdentifierProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignupService implements UserSignupUseCase {

    private final AuthMemberGateway memberRegistrationUseCase;
    private final IdentifierProvider identifierProvider;

    @Override
    public void initializeAdminUser(SignupAdminCommand signupAdminCommand) {
        if(memberRegistrationUseCase.adminExists()) {
            return;
        }
        memberRegistrationUseCase.register(new AuthMemberRegistration(identifierProvider.generateIdentifier(),
                signupAdminCommand.username(), signupAdminCommand.introduction(), signupAdminCommand.positionList(),
                signupAdminCommand.skillList(), MemberRole.ADMIN));
    }

    @Override
    public void saveEmailUserInfo(SignupUserCommand signupUserCommand, String userGuid) {
        memberRegistrationUseCase.register(new AuthMemberRegistration(userGuid, signupUserCommand.username(),
                signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList(), MemberRole.USER));
    }

    @Override
    public void saveOAuthUserInfo(SignupOAuthUserCommand signupOAuthUserCommand, String userGuid) {
        memberRegistrationUseCase.register(new AuthMemberRegistration(userGuid, signupOAuthUserCommand.username(),
                signupOAuthUserCommand.introduction(), signupOAuthUserCommand.positionList(), signupOAuthUserCommand.skillList(), MemberRole.USER));
    }
}
