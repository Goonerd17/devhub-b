package teamdevhub.devhub.medium.outbound.auth.adapter;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import teamdevhub.devhub.identity.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.identity.outbound.auth.adapter.RefreshTokenAdapter;
import teamdevhub.devhub.identity.outbound.auth.adapter.entity.RefreshTokenEntity;
import teamdevhub.devhub.identity.outbound.auth.persistence.JpaRefreshTokenRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@Transactional
class RefreshTokenAdapterTest {

    @Autowired
    private RefreshTokenAdapter refreshTokenAdapter;

    @Autowired
    private JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @BeforeEach
    void init() {
        jpaRefreshTokenRepository.deleteAll();
    }

    @Test
    @DisplayName("由ы봽?덉떆_?좏겙????ν븳??")
    void saveRefreshToken() {
        // given
        RefreshToken refreshToken = RefreshToken.of("USER_GUID_1", "REFRESH_TOKEN_1");

        // when
        refreshTokenAdapter.save(refreshToken);

        // then
        RefreshTokenEntity refreshTokenEntity = jpaRefreshTokenRepository.findByUserGuid("USER_GUID_1").orElseThrow();

        assertThat(refreshTokenEntity.getUserGuid()).isEqualTo("USER_GUID_1");
        assertThat(refreshTokenEntity.getToken()).isEqualTo("REFRESH_TOKEN_1");
    }

    @Test
    @DisplayName("?대?_議댁옱?섎뒗_由ы봽?덉떆_?좏겙???덈떎硫??좏겙??蹂寃쎌떆?⑤떎")
    void saveRefreshToken_rotate() {
        // given
        jpaRefreshTokenRepository.save(RefreshTokenEntity.of("USER_GUID_1", "OLD_REFRESH_TOKEN"));
        RefreshToken refreshToken = RefreshToken.of("USER_GUID_1", "NEW_REFRESH_TOKEN");

        // when
        refreshTokenAdapter.save(refreshToken);

        // then
        RefreshTokenEntity refreshTokenEntity = jpaRefreshTokenRepository.findByUserGuid("USER_GUID_1").orElseThrow();

        assertThat(refreshTokenEntity.getToken()).isEqualTo("NEW_REFRESH_TOKEN");
    }

    @Test
    @DisplayName("?ъ슜???앸퀎?ㅻ줈_由ы봽?덉떆_?좏겙??議고쉶?쒕떎")
    void findByUserGuid() {
        // given
        jpaRefreshTokenRepository.save(RefreshTokenEntity.of("USER_GUID_1", "REFRESH_TOKEN_1"));

        // when
        Optional<RefreshToken> refreshToken = refreshTokenAdapter.findByUserGuid("USER_GUID_1");

        // then
        assertThat(refreshToken.get().userGuid()).isEqualTo("USER_GUID_1");
        assertThat(refreshToken.get().token()).isEqualTo("REFRESH_TOKEN_1");
    }

    @Test
    @DisplayName("由ы봽?덉떆 ?좏겙???놁쑝硫?Optional.empty 瑜?諛섑솚?쒕떎")
    void findByUserGuid_notExists_returnsEmpty() {
        // given
        String userGuid = "NOT_EXIST_USER";

        // when
        Optional<RefreshToken> result = refreshTokenAdapter.findByUserGuid(userGuid);

        // then
        assertThat(result).isEmpty();
    }
    @Test
    @DisplayName("?ъ슜???앸퀎?ㅻ줈_由ы봽?덉떆_?좏겙????젣?쒕떎")
    void deleteByUserGuid() {
        // given
        jpaRefreshTokenRepository.save(RefreshTokenEntity.of("USER_GUID_1", "REFRESH_TOKEN_1"));

        // when
        refreshTokenAdapter.deleteByUserGuid("USER_GUID_1");

        // then
        assertThat(jpaRefreshTokenRepository.findByUserGuid("USER_GUID_1")).isEmpty();
    }
}
