package teamdevhub.devhub.identity.core.auth.port.out.token;

import teamdevhub.devhub.identity.core.auth.port.out.token.vo.AccessTokenInfo;
import teamdevhub.devhub.identity.core.auth.port.out.token.vo.TempTokenInfo;

public interface TokenParseProvider {

    AccessTokenInfo getAccessTokenInfo(String accessToken);
    TempTokenInfo getTempTokenInfo(String tempToken);
    String getRefreshTokenInfo(String refreshToken);
    String removeBearer(String token);
}

