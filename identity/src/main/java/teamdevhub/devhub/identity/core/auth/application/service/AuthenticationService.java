package teamdevhub.devhub.identity.core.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.identity.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.identity.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.identity.core.auth.port.out.token.RefreshTokenRepository;
import teamdevhub.devhub.identity.core.auth.port.out.token.TokenIssueProvider;
import teamdevhub.devhub.member.api.CurrentMemberRoleQuery;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {

    private final TokenIssueProvider tokenIssueProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CurrentMemberRoleQuery currentMemberRoleQuery;

    @Override
    public AuthResult login(AuthenticatedUser authenticatedUser) {
        AuthenticatedUser currentUser = withCurrentRole(authenticatedUser);
        String accessToken = tokenIssueProvider.createAccessToken(currentUser);
        String refreshToken = tokenIssueProvider.createRefreshToken(currentUser.userGuid());
        issueRefreshToken(currentUser.userGuid(), refreshToken);
        return AuthResult.of(accessToken, refreshToken);
    }

    @Override
    public AuthResult reissueAccessToken(AuthenticatedUser authenticatedUser) {
        AuthenticatedUser currentUser = withCurrentRole(authenticatedUser);
        String newAccessToken = tokenIssueProvider.createAccessToken(currentUser);
        String newRefreshToken = tokenIssueProvider.createRefreshToken(currentUser.userGuid());
        issueRefreshToken(currentUser.userGuid(), newRefreshToken);
        return AuthResult.of(newAccessToken, newRefreshToken);
    }

    private AuthenticatedUser withCurrentRole(AuthenticatedUser authenticatedUser) {
        return AuthenticatedUser.of(authenticatedUser.userGuid(), authenticatedUser.loginId(),
                currentMemberRoleQuery.findCurrentRole(authenticatedUser.userGuid()));
    }

    @Override
    public void revoke(String userGuid) {
        refreshTokenRepository.deleteByUserGuid(userGuid);
    }

    private void issueRefreshToken(String userGuid, String token) {
        RefreshToken refreshToken = RefreshToken.of(userGuid, token);
        refreshTokenRepository.save(refreshToken);
    }
}
