package teamdevhub.devhub.web.api.auth.model.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.web.api.auth.model.response.TokenResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

class TokenResponseDtoTest {

    @Test
    @DisplayName("TokenResponseDto_를_생성할_수_있다")
    void canIssueResponseDto() {
        // given
        String accessToken = "access-token-random";

        // when
        TokenResponseDto tokenResponseDto = TokenResponseDto.issueAccessToken(accessToken);

        // then
        assertThat(tokenResponseDto).isNotNull();
        assertThat(accessToken).isEqualTo(tokenResponseDto.getAccessToken());
    }
}
