package teamdevhub.devhub.auth.core.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.auth.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.auth.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.auth.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.auth.core.auth.port.out.AuthenticatedUserResolver;
import teamdevhub.devhub.auth.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.auth.core.auth.port.out.password.EncodedPasswordProvider;
import teamdevhub.devhub.auth.core.auth.port.out.token.RefreshTokenRepository;
import teamdevhub.devhub.auth.core.auth.port.out.token.TokenParseProvider;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.identifier.IdentifierProvider;
import teamdevhub.devhub.shared.security.MemberRole;
import teamdevhub.devhub.auth.api.credential.EmailCredentialRegistrationCommand;
import teamdevhub.devhub.auth.api.credential.UpdatePasswordCommand;
import teamdevhub.devhub.auth.api.credential.AdminPasswordReset;
import teamdevhub.devhub.auth.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCredentialService implements UserCredentialUseCase, AdminPasswordReset, teamdevhub.devhub.shared.security.AdminPasswordReset {

    private final TokenParseProvider tokenParseProvider;
    private final IdentifierProvider identifierProvider;
    private final EncodedPasswordProvider encodedPasswordProvider;
    private final AuthenticatedUserResolver authenticatedUserResolver;
    private final UserCredentialRepository userCredentialRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String signupEmailUser(EmailCredentialRegistrationCommand command) {
        userCredentialRepository.findEmailUserCredentialByEmail(command.email())
                .ifPresent(existingCredential -> {
                    throw BusinessRuleException.of(ErrorCode.DUPLICATED_ACCOUNT);
                });

        String userGuid = identifierProvider.generateIdentifier();
        String encryptedPassword = encodedPasswordProvider.encode(command.password());
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(userGuid, command.email(), MemberRole.USER);

        userCredentialRepository.saveEmailUserCredential(authenticatedUser, encryptedPassword);
        return userGuid;
    }

    @Override
    public AuthenticatedUser signupOAuthUser(OAuthUser oauthUser) {
        userCredentialRepository.findOAuthUserCredentialByOAuth(oauthUser.verificationProvider(), oauthUser.oauthId())
                .ifPresent(existingCredential -> {
                    throw BusinessRuleException.of(ErrorCode.DUPLICATED_ACCOUNT);
                });

        String userGuid = identifierProvider.generateIdentifier();
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(userGuid, oauthUser.oauthId(), MemberRole.USER);

        userCredentialRepository.saveOAuthUserCredential(authenticatedUser, oauthUser.verificationProvider(), oauthUser.oauthId());
        return authenticatedUser;
    }

    @Override
    public AuthenticatedUser getUserForReissue(String refreshToken) {
        String userGuid = tokenParseProvider.getRefreshTokenInfo(refreshToken);
        RefreshToken savedRefreshToken = refreshTokenRepository.findByUserGuid(userGuid)
                .orElseThrow(() -> BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID));

        if (!savedRefreshToken.token().equals(refreshToken)) {
            throw BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        return userCredentialRepository.findUserCredentialByUserGuid(userGuid)
                .orElseThrow(() -> BusinessRuleException.of(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public AuthenticatedUser authenticate(LoginCommand loginCommand) {
        return authenticatedUserResolver.getAuthenticatedUser(loginCommand.email(), loginCommand.password());
    }

    @Override
    public void updatePassword(UpdatePasswordCommand updatePasswordCommand) {
        EmailUserCredential emailUserCredential = userCredentialRepository.findEmailCredentialByUserGuid(updatePasswordCommand.userGuid());
        emailUserCredential.verifyPassword(encodedPasswordProvider.matches(updatePasswordCommand.currentPassword(), emailUserCredential.getPassword()));
        emailUserCredential.changePassword(encodedPasswordProvider.encode(updatePasswordCommand.newPassword()));
        userCredentialRepository.savePassword(emailUserCredential);
    }

    @Override
    public void resetUserPassword(String userGuid, String newPassword) {
        EmailUserCredential emailUserCredential = userCredentialRepository.findEmailCredentialByUserGuid(userGuid);
        emailUserCredential.changePassword(encodedPasswordProvider.encode(newPassword));
        userCredentialRepository.savePassword(emailUserCredential);
    }

    @Override
    public void reset(String userGuid, String newPassword) {
        resetUserPassword(userGuid, newPassword);
    }

}
