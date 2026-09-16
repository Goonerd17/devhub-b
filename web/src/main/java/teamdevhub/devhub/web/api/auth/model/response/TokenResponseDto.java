package teamdevhub.devhub.web.api.auth.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {

    private String accessToken;
    private String tempToken;

    public static TokenResponseDto issueAccessToken(String accessToken) {
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }

    public static TokenResponseDto issueTempToken(String tempToken) {
        return TokenResponseDto.builder()
                .tempToken(tempToken)
                .build();
    }
}