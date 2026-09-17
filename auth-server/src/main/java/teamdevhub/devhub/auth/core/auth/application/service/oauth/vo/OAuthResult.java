package teamdevhub.devhub.auth.core.auth.application.service.oauth.vo;

import lombok.Builder;
import teamdevhub.devhub.auth.core.auth.application.service.AuthResult;
import teamdevhub.devhub.auth.core.auth.application.service.oauth.SignupStatus;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;

@Builder
public record OAuthResult(
        SignupStatus signupStatus,
        String accessToken,
        String refreshToken,
        String tempToken
) {

    public static OAuthResult loggedIn(AuthResult authResult) {
        return new OAuthResult(
                SignupStatus.COMPLETED,
                authResult.accessToken(),
                authResult.refreshToken(),
                null
        );
    }

    public static OAuthResult requiresSignup(String tempToken) {
        return new OAuthResult(
                SignupStatus.PENDING,
                null,
                null,
                tempToken
        );
    }

    public String toauthorizationHeader() {
        if (accessToken == null) {
            throw BusinessRuleException.of(ErrorCode.TOKEN_INVALID);
        }
        return "Bearer " + accessToken;
    }
}
