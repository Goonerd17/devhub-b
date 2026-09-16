package teamdevhub.devhub.member.outbound.user.adapter.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.member.outbound.user.adapter.entity.UserPositionEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserPositionEntityTest {

    private static final String TEST_POSITION_CD = "001";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_USER_POSITION_GUID = "POSITION1a1b2c3d4e5f6g7h8i9j10k";

    @Test
    @DisplayName("UserPositionEntity_를_생성하고_getter_를_사용할_수_있다")
    void createEntityAndUseGetter() {
        // given, when
        UserPositionEntity userPositionEntity = UserPositionEntity.builder()
                .userPositionGuid(TEST_USER_POSITION_GUID)
                .userGuid(TEST_USER_GUID_1)
                .positionCd(TEST_POSITION_CD)
                .build();

        // then
        assertThat(userPositionEntity.getUserPositionGuid()).isEqualTo(TEST_USER_POSITION_GUID);
        assertThat(userPositionEntity.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userPositionEntity.getPositionCd()).isEqualTo(TEST_POSITION_CD);
    }
}
