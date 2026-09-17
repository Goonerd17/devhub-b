package teamdevhub.devhub.fake.pure.application.port.out.auth;

import teamdevhub.devhub.auth.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.shared.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * FakeUserCredentialRepository? ?щ━ findEmailCredentialByUserGuid 諛?savePassword瑜??꾩쟾??援ы쁽??Fake.
 * 鍮꾨?踰덊샇 蹂寃?珥덇린???뚯뒪?몄뿉???ъ슜?쒕떎.
 */
public class FakeFullUserCredentialRepository implements UserCredentialRepository {

    private final Map<String, AuthenticatedUser> byGuid = new HashMap<>();
    private final Map<String, AuthenticatedUser> byEmail = new HashMap<>();
    private final Map<String, AuthenticatedUser> byOAuth = new HashMap<>();
    private final Map<String, EmailUserCredential> emailCredentialByGuid = new HashMap<>();

    @Override
    public Optional<AuthenticatedUser> findUserCredentialByUserGuid(String userGuid) {
        return Optional.ofNullable(byGuid.get(userGuid));
    }

    @Override
    public Optional<AuthenticatedUser> findEmailUserCredentialByEmail(String email) {
        return Optional.ofNullable(byEmail.get(email));
    }

    @Override
    public Optional<AuthenticatedUser> findOAuthUserCredentialByOAuth(VerificationProvider verificationProvider, String oauthId) {
        String key = verificationProvider.name() + ":" + oauthId;
        return Optional.ofNullable(byOAuth.get(key));
    }

    @Override
    public void saveEmailUserCredential(AuthenticatedUser authenticatedUser, String encryptedPassword) {
        byGuid.put(authenticatedUser.userGuid(), authenticatedUser);
        byEmail.put(authenticatedUser.loginId(), authenticatedUser);
        EmailUserCredential emailUserCredential = EmailUserCredential.of(
                authenticatedUser.userGuid(),
                authenticatedUser.loginId(),
                encryptedPassword,
                MemberRole.USER
        );
        emailCredentialByGuid.put(authenticatedUser.userGuid(), emailUserCredential);
    }

    @Override
    public void saveOAuthUserCredential(AuthenticatedUser authenticatedUser, VerificationProvider verificationProvider, String oauthId) {
        String key = verificationProvider.name() + ":" + oauthId;
        byOAuth.put(key, authenticatedUser);
    }

    @Override
    public EmailUserCredential findEmailCredentialByUserGuid(String userGuid) {
        EmailUserCredential credential = emailCredentialByGuid.get(userGuid);
        if (credential == null) {
            throw AdapterDataException.of(ErrorCode.USER_NOT_FOUND);
        }
        return credential;
    }

    @Override
    public void savePassword(EmailUserCredential emailUserCredential) {
        emailCredentialByGuid.put(emailUserCredential.getUserGuid(), emailUserCredential);
    }
}
