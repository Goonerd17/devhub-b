package teamdevhub.devhub.auth.core.auth.port.out;

import teamdevhub.devhub.auth.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

import java.util.Optional;

public interface UserCredentialRepository {

    Optional<AuthenticatedUser> findUserCredentialByUserGuid(String userGuid);
    Optional<AuthenticatedUser> findEmailUserCredentialByEmail(String email);
    Optional<AuthenticatedUser> findOAuthUserCredentialByOAuth(VerificationProvider verificationProvider, String oauthId);
    void saveEmailUserCredential(AuthenticatedUser authenticatedUser, String encryptedPassword);
    void saveOAuthUserCredential(AuthenticatedUser authenticatedUser, VerificationProvider verificationProvider, String oauthId);
    EmailUserCredential findEmailCredentialByUserGuid(String userGuid);
    void savePassword(EmailUserCredential emailUserCredential);
}
