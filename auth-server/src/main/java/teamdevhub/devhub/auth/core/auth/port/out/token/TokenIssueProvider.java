package teamdevhub.devhub.auth.core.auth.port.out.token;

import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

public interface TokenIssueProvider {

    String createAccessToken(AuthenticatedUser authenticatedUser);
    String createRefreshToken(String email);
    String createTempToken(String oauthId, VerificationProvider verificationProvider, String email);
}