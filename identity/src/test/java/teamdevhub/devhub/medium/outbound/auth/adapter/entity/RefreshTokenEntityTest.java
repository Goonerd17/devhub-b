package teamdevhub.devhub.identity.outbound.auth.adapter.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.outbound.auth.adapter.entity.RefreshTokenEntity;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenEntityTest {

    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";

    @Test
    @DisplayName("RefreshTokenEntity_를_생성자로_생성하면_userGuid_와_token_이_올바르게_설정된다")
    void testConstructorAndGetters() {
        // given
        String token = "token-abc";

        // when
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .userGuid(TEST_USER_GUID_1)
                .token(token)
                .build();

        // then
        assertThat(refreshTokenEntity.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(refreshTokenEntity.getToken()).isEqualTo(token);
    }

    @Test
    @DisplayName("팩토리_메서드_of_를_통해_RefreshTokenEntity_를_생성하면_필드_값이_올바르게_설정된다")
    void testStaticFactoryOf() {
        // given
        String token = "token-def";

        // when
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.of(TEST_USER_GUID_1, token);

        // then
        assertThat(refreshTokenEntity.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(refreshTokenEntity.getToken()).isEqualTo(token);
    }
}
