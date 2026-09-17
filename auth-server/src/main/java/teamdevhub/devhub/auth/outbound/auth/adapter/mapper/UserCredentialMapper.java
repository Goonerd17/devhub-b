package teamdevhub.devhub.auth.outbound.auth.adapter.mapper;

import teamdevhub.devhub.auth.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.outbound.auth.adapter.entity.EmailCredentialEntity;
import teamdevhub.devhub.auth.outbound.auth.adapter.entity.OAuthCredentialEntity;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

public class UserCredentialMapper {

    public static AuthenticatedUser toAuthenticatedUser(EmailCredentialEntity emailCredentialEntity) {
        return AuthenticatedUser.of(
                emailCredentialEntity.getUserGuid(),
                emailCredentialEntity.getEmail(),
                emailCredentialEntity.getUserRole()
        );
    }

    public static AuthenticatedUser toAuthenticatedUser(OAuthCredentialEntity oauthCredentialEntity) {
        return AuthenticatedUser.of(
                oauthCredentialEntity.getUserGuid(),
                oauthCredentialEntity.getOauthId(),
                oauthCredentialEntity.getUserRole()
        );
    }

    public static EmailUserCredential toEmailUserCredential(EmailCredentialEntity emailCredentialEntity) {
        return EmailUserCredential.of(
                emailCredentialEntity.getUserGuid(),
                emailCredentialEntity.getEmail(),
                emailCredentialEntity.getPassword(),
                emailCredentialEntity.getUserRole()
        );
    }

    public static EmailCredentialEntity toEmailCredentialEntity(EmailUserCredential emailUserCredential) {
        return EmailCredentialEntity.builder()
                .userGuid(emailUserCredential.getUserGuid())
                .email(emailUserCredential.getEmail())
                .password(emailUserCredential.getPassword())
                .userRole(emailUserCredential.getUserRole())
                .build();
    }

    public static EmailCredentialEntity toEmailCredentialEntity(
            AuthenticatedUser authenticatedUser,
            String encryptedPassword
    ) {
        return EmailCredentialEntity.builder()
                .userGuid(authenticatedUser.userGuid())
                .email(authenticatedUser.loginId())
                .password(encryptedPassword)
                .userRole(authenticatedUser.userRole())
                .build();
    }

    public static OAuthCredentialEntity toOAuthCredentialEntity(
            AuthenticatedUser authenticatedUser,
            VerificationProvider verificationProvider,
            String oauthId
    ) {
        return OAuthCredentialEntity.builder()
                .userGuid(authenticatedUser.userGuid())
                .provider(verificationProvider)
                .oauthId(oauthId)
                .userRole(authenticatedUser.userRole())
                .build();
    }
}
