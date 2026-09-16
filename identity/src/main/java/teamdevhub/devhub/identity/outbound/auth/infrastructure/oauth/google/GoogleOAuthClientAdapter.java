package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.google;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import teamdevhub.devhub.identity.core.auth.port.out.oauth.OAuthClient;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.OAuthHttpClient;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.BearerAuthHeaderProvider;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.DefaultHeaderProvider;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.HttpResponse;
import teamdevhub.devhub.identity.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.google.config.GoogleOAuthConfig;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.google.vo.GoogleTokenResponse;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.google.vo.GoogleUserResponse;
import teamdevhub.devhub.platform.outbound.common.exception.ExternalServiceException;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class GoogleOAuthClientAdapter implements OAuthClient {

    private final OAuthHttpClient oauthHttpClient;
    private final GoogleOAuthConfig googleOAuthConfig;

    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return verificationProvider == VerificationProvider.GOOGLE;
    }

    @Override
    public String getAuthorizationUrl(String state) {
        return UriComponentsBuilder
                .fromUri(URI.create(googleOAuthConfig.getAuthorizationUri()))
                .queryParam("client_id", googleOAuthConfig.getClientId())
                .queryParam("redirect_uri", googleOAuthConfig.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "profile email")
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public OAuthUser fetchUser(String code) {
        String accessToken = getAccessToken(code);

        HttpResponse<GoogleUserResponse> response = oauthHttpClient.get(googleOAuthConfig.getUserInfoUri(), new BearerAuthHeaderProvider(accessToken), GoogleUserResponse.class);

        if (!response.is2xx() || response.body() == null) {
            throw ExternalServiceException.of(ErrorCode.UNKNOWN_FAIL);
        }

        GoogleUserResponse googleUser = response.body();

        return new OAuthUser(googleUser.id(), VerificationProvider.GOOGLE, googleUser.email());
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add("code", code);
        form.add("client_id", googleOAuthConfig.getClientId());
        form.add("client_secret", googleOAuthConfig.getClientSecret());
        form.add("redirect_uri", googleOAuthConfig.getRedirectUri());
        form.add("grant_type", "authorization_code");

        HttpResponse<GoogleTokenResponse> googleToken = oauthHttpClient.postFormUrlEncoded(googleOAuthConfig.getTokenUri(), form, new DefaultHeaderProvider(), GoogleTokenResponse.class);

        if (!googleToken.is2xx() || googleToken.body() == null || googleToken.body().access_token() == null) {
            throw ExternalServiceException.of(ErrorCode.UNKNOWN_FAIL);
        }

        return googleToken.body().access_token();
    }
}