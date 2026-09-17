package teamdevhub.devhub.fake.pure.application.port.out.auth;

import teamdevhub.devhub.auth.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeUserCredentialRepository implements UserCredentialRepository {

    private final Map<String, AuthenticatedUser> emailByGuidStore = new HashMap<>();
    private final Map<String, AuthenticatedUser> emailByEmailStore = new HashMap<>();
    private final Map<String, AuthenticatedUser> oauthStore = new HashMap<>();

    @Override
    public Optional<AuthenticatedUser> findUserCredentialByUserGuid(String userGuid) {
        return Optional.ofNullable(emailByGuidStore.get(userGuid));
    }

    @Override
    public Optional<AuthenticatedUser> findEmailUserCredentialByEmail(String email) {
        return Optional.ofNullable(emailByEmailStore.get(email));
    }

    @Override
    public Optional<AuthenticatedUser> findOAuthUserCredentialByOAuth(VerificationProvider verificationProvider, String oauthId) {
        String key = verificationProvider.name() + ":" + oauthId;
        return Optional.ofNullable(oauthStore.get(key));
    }

    @Override
    public void saveEmailUserCredential(AuthenticatedUser authenticatedUser, String encryptedPassword) {
        emailByGuidStore.put(authenticatedUser.userGuid(), authenticatedUser);
        emailByEmailStore.put(authenticatedUser.loginId(), authenticatedUser);
    }

    @Override
    public void saveOAuthUserCredential(AuthenticatedUser authenticatedUser, VerificationProvider verificationProvider, String oauthId) {
        String key = verificationProvider.name() + ":" + oauthId;
        oauthStore.put(key, authenticatedUser);
    }

    @Override
    public EmailUserCredential findEmailCredentialByUserGuid(String userGuid) {
        return null;
    }

    @Override
    public void savePassword(EmailUserCredential emailUserCredential) {

    }
}
