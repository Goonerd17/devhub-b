package teamdevhub.devhub.auth.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.auth.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.auth.core.user.port.in.usecase.UserSignupUseCase;
import teamdevhub.devhub.member.api.MemberRegistrationCommand;
import teamdevhub.devhub.member.api.MemberRegistrationUseCase;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.shared.identifier.IdentifierProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignupService implements UserSignupUseCase {

    private final MemberRegistrationUseCase memberRegistrationUseCase;
    private final IdentifierProvider identifierProvider;

    @Override
    public void initializeAdminUser(SignupAdminCommand signupAdminCommand) {
        if(memberRegistrationUseCase.adminExists()) {
            return;
        }
        memberRegistrationUseCase.register(new MemberRegistrationCommand(identifierProvider.generateIdentifier(),
                signupAdminCommand.username(), signupAdminCommand.introduction(), signupAdminCommand.positionList(),
                signupAdminCommand.skillList(), MemberRole.ADMIN));
    }

    @Override
    public void saveEmailUserInfo(SignupUserCommand signupUserCommand, String userGuid) {
        memberRegistrationUseCase.register(new MemberRegistrationCommand(userGuid, signupUserCommand.username(),
                signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList(), MemberRole.USER));
    }

    @Override
    public void saveOAuthUserInfo(SignupOAuthUserCommand signupOAuthUserCommand, String userGuid) {
        memberRegistrationUseCase.register(new MemberRegistrationCommand(userGuid, signupOAuthUserCommand.username(),
                signupOAuthUserCommand.introduction(), signupOAuthUserCommand.positionList(), signupOAuthUserCommand.skillList(), MemberRole.USER));
    }
}
