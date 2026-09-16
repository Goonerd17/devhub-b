package teamdevhub.devhub.fake.pure.application.provider;

import teamdevhub.devhub.identity.core.auth.port.out.token.vo.AccessTokenInfo;
import teamdevhub.devhub.identity.core.auth.port.out.token.vo.TempTokenInfo;
import teamdevhub.devhub.identity.core.auth.port.out.token.TokenParseProvider;

import java.util.HashMap;
import java.util.Map;

public class FakeTokenParseProvider implements TokenParseProvider {

    private final Map<String, AccessTokenInfo> accessTokenStore = new HashMap<>();
    private final Map<String, TempTokenInfo> tempTokenStore = new HashMap<>();
    private final Map<String, String> refreshTokenStore = new HashMap<>();

    public void givenAccessToken(String accessToken, AccessTokenInfo tokenInfo) {
        accessTokenStore.put(accessToken, tokenInfo);
    }

    public void givenTempToken(String tempToken, TempTokenInfo tokenInfo) {
        tempTokenStore.put(tempToken, tokenInfo);
    }

    public void givenRefreshToken(String refreshToken, String userGuid) {
        refreshTokenStore.put(refreshToken, userGuid);
    }

    @Override
    public AccessTokenInfo getAccessTokenInfo(String accessToken) {
        AccessTokenInfo info = accessTokenStore.get(accessToken);
        if (info == null) {
            throw new IllegalArgumentException("등록되지 않은 access token: " + accessToken);
        }
        return info;
    }

    @Override
    public TempTokenInfo getTempTokenInfo(String tempToken) {
        TempTokenInfo info = tempTokenStore.get(tempToken);
        if (info == null) {
            throw new IllegalArgumentException("등록되지 않은 temp token: " + tempToken);
        }
        return info;
    }

    @Override
    public String getRefreshTokenInfo(String refreshToken) {
        String userGuid = refreshTokenStore.get(refreshToken);
        if (userGuid == null) {
            throw new IllegalArgumentException("등록되지 않은 refresh token: " + refreshToken);
        }
        return userGuid;
    }

    @Override
    public String removeBearer(String token) {
        if (token == null) return null;
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }
}
