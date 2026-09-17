package teamdevhub.devhub.auth.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

@Getter
public class OAuthUserCredential {

    private final String userGuid;
    private final String oauthId;
    private final VerificationProvider verificationProvider;
    private final MemberRole userRole;

    @Builder
    public OAuthUserCredential(
            String userGuid,
            String oauthId,
            VerificationProvider verificationProvider,
            MemberRole userRole
    ) {
        this.userGuid = userGuid;
        this.oauthId = oauthId;
        this.verificationProvider = verificationProvider;
        this.userRole = userRole;
    }

    public static OAuthUserCredential of(
            String userGuid,
            String oauthId,
            VerificationProvider verificationProvider,
            MemberRole userRole
    ) {
        return OAuthUserCredential.builder()
                .userGuid(userGuid)
                .oauthId(oauthId)
                .verificationProvider(verificationProvider)
                .userRole(userRole)
                .build();
    }
}
