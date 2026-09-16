package teamdevhub.devhub.identity.core.auth.application.service.oauth.vo;

import lombok.Builder;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

@Builder
public record OAuthUserResult(boolean loginAvailable, AuthenticatedUser authenticatedUser) {

    public static OAuthUserResult success(AuthenticatedUser authenticatedUser) {
        return new OAuthUserResult(true, authenticatedUser);
    }

    public static OAuthUserResult requiresSignup() {
        return new OAuthUserResult(false, null);
    }

    public AuthenticatedUser requireAuthenticatedUser() {
        if (!loginAvailable) {
            throw BusinessRuleException.of(ErrorCode.UNKNOWN_FAIL);
        }
        return authenticatedUser;
    }
}
