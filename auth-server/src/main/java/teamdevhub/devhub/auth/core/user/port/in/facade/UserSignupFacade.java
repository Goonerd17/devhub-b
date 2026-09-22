package teamdevhub.devhub.auth.core.user.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.auth.core.auth.application.service.AuthResult;
import teamdevhub.devhub.auth.core.auth.application.service.oauth.vo.OAuthResult;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.shared.terms.TermsAgreementGateway;
import teamdevhub.devhub.auth.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.shared.security.MemberRole;
import teamdevhub.devhub.auth.api.credential.EmailCredentialRegistrationCommand;
import teamdevhub.devhub.shared.member.AuthMemberGateway;
import teamdevhub.devhub.auth.core.user.port.in.usecase.UserSignupUseCase;
import teamdevhub.devhub.auth.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.auth.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.auth.core.auth.port.in.usecase.oauth.OAuthResolveUseCase;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.auth.core.auth.port.in.usecase.verification.VerificationUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignupFacade {

    private final UserSignupUseCase userSignupUseCase;
    private final TermsAgreementGateway termsUseCase;
    private final OAuthResolveUseCase oauthResolveUseCase;
    private final UserCredentialUseCase userCredentialUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final VerificationUseCase verificationUseCase;
    private final AuthMemberGateway userLoginUseCase;

    public AuthResult signup(SignupUserCommand signupUserCommand) {
        verificationUseCase.assertAllowed(signupUserCommand.verificationTarget());
        String userGuid = userCredentialUseCase.signupEmailUser(
                new EmailCredentialRegistrationCommand(signupUserCommand.email(), signupUserCommand.password()));
        userSignupUseCase.saveEmailUserInfo(signupUserCommand, userGuid);
        termsUseCase.save(signupUserCommand.toAgreeTermsCommand(userGuid));
        verificationUseCase.consume(signupUserCommand.verificationTarget());
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(userGuid, signupUserCommand.email(), MemberRole.USER);
        userLoginUseCase.recordSuccessfulLogin(userGuid);
        return authenticationUseCase.login(authenticatedUser);
    }

    public OAuthResult signupWithOAuth(SignupOAuthUserCommand signupOAuthUserCommand) {
        OAuthUser oauthUser = oauthResolveUseCase.extractOAuthUser(signupOAuthUserCommand);
        AuthenticatedUser authenticatedUser = userCredentialUseCase.signupOAuthUser(oauthUser);
        userSignupUseCase.saveOAuthUserInfo(signupOAuthUserCommand, authenticatedUser.userGuid());
        termsUseCase.save(signupOAuthUserCommand.toAgreeTermsCommand(authenticatedUser.userGuid()));
        AuthResult authResult = authenticationUseCase.login(authenticatedUser);
        return OAuthResult.loggedIn(authResult);
    }

}
