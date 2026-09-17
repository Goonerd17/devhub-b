package teamdevhub.devhub.member.outbound.user.adapter.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserPositionEntity;
import teamdevhub.devhub.member.outbound.user.adapter.mapper.UserPositionMapper;

import static org.assertj.core.api.Assertions.assertThat;

class UserPositionMapperTest {

    private static final String TEST_POSITION_CD = "001";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_USER_POSITION_GUID = "POSITION1a1b2c3d4e5f6g7h8i9j10k";

    @Test
    @DisplayName("레코드에서_엔티티로_전환한다")
    void toEntity_convertsRecordToEntityCorrectly() {
        // given
        UserPosition userPosition = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);

        // when
        UserPositionEntity userPositionEntity = UserPositionMapper.toEntity(TEST_USER_POSITION_GUID, userPosition);

        // then
        assertThat(userPositionEntity.getUserPositionGuid()).isEqualTo(TEST_USER_POSITION_GUID);
        assertThat(userPositionEntity.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userPositionEntity.getPositionCd()).isEqualTo(TEST_POSITION_CD);
    }

    @Test
    @DisplayName("엔티티에서_레코드로_전환한다")
    void toRecord_convertsEntityToRecordCorrectly() {
        // given
        UserPositionEntity userPositionEntity = UserPositionEntity.builder()
                .userPositionGuid(TEST_USER_POSITION_GUID)
                .userGuid(TEST_USER_GUID_1)
                .positionCd(TEST_POSITION_CD)
                .build();

        // when
        UserPosition userPosition = UserPositionMapper.toRecord(userPositionEntity);

        // then
        assertThat(userPosition.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userPosition.positionCd()).isEqualTo(TEST_POSITION_CD);
    }

}
