package teamdevhub.devhub.medium.outbound.user.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import teamdevhub.devhub.member.outbound.user.adapter.UserPositionAdapter;
import teamdevhub.devhub.member.outbound.user.persistence.JpaUserPositionRepository;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@Transactional
class UserPositionAdapterTest {

    @Autowired
    private UserPositionAdapter userPositionAdapter;

    @Autowired
    private JpaUserPositionRepository jpaUserPositionRepository;

    @BeforeEach
    void init() {
        jpaUserPositionRepository.deleteAll();
    }

    @Test
    @DisplayName("?꾩껜_愿???ъ??섏쓣_??ν븳??")
    void saveAll_savesPositionsCorrectly() {
        // given
        UserPosition pos1 = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        UserPosition pos2 = new UserPosition(TEST_USER_GUID_1, NEW_POSITION_CD);

        Set<UserPosition> positions = new HashSet<>(Set.of(pos1, pos2));

        // when
        userPositionAdapter.saveAll(positions);

        // then
        Set<UserPosition> saved = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(saved).hasSize(2)
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrder(TEST_POSITION_CD, NEW_POSITION_CD);
    }

    @Test
    @DisplayName("saveAll_?_鍮?Set_?대㈃_?꾨Т寃껊룄_??ν븯吏_?딅뒗??")
    void saveAll_emptySet_doesNothing() {
        // given
        Set<UserPosition> emptyPositions = new HashSet<>();

        // when
        userPositionAdapter.saveAll(emptyPositions);

        // then
        Set<UserPosition> saved = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(saved).isEmpty();
    }

    @Test
    @DisplayName("愿?ы룷吏?섏씠_蹂寃쎈릺硫?紐⑤뱺_蹂寃쎌궗??씠_諛섏쁺?쒕떎")
    void replace_mergesOldAndNewPositionsCorrectly() {
        // given
        UserPosition oldPos = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        Set<UserPosition> previousPositions = new HashSet<>(Set.of(oldPos));
        userPositionAdapter.saveAll(previousPositions);

        UserPosition newPos = new UserPosition(TEST_USER_GUID_1, NEW_POSITION_CD);
        Set<UserPosition> currentPositions = new HashSet<>(Set.of(oldPos, newPos));

        // when
        userPositionAdapter.replace(previousPositions, currentPositions);

        // then
        Set<UserPosition> finalPositions = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(finalPositions).hasSize(2)
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrder(TEST_POSITION_CD, NEW_POSITION_CD);
    }

    @Test
    @DisplayName("蹂寃쎌궗??뿉????젣??媛믪쑝濡??앸퀎???ъ???媛믪?_??젣?쒕떎")
    void replace_removesDeletedPositionsCorrectly() {
        // given
        UserPosition oldPos1 = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        UserPosition oldPos2 = new UserPosition(TEST_USER_GUID_1, NEW_POSITION_CD);

        Set<UserPosition> previousPositions =
                new HashSet<>(Set.of(oldPos1, oldPos2));

        userPositionAdapter.saveAll(previousPositions);

        Set<UserPosition> currentPositions =
                new HashSet<>(Set.of(oldPos1));

        // when
        userPositionAdapter.replace(previousPositions, currentPositions);

        // then
        Set<UserPosition> finalPositions = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(finalPositions).hasSize(1)
                .extracting(UserPosition::positionCd)
                .containsExactly(TEST_POSITION_CD);
    }

    @Test
    @DisplayName("蹂寃쎌궗??씠_議댁옱?섏?_?딆쑝硫?蹂寃쎈릺吏_?딅뒗??")
    void replace_noChanges_doesNothing() {
        // given
        UserPosition pos = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        Set<UserPosition> positions = new HashSet<>(Set.of(pos));
        userPositionAdapter.saveAll(positions);

        // when
        userPositionAdapter.replace(positions, positions);

        // then
        Set<UserPosition> finalPositions =
                userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(finalPositions).hasSize(1)
                .extracting(UserPosition::positionCd)
                .containsExactly(TEST_POSITION_CD);
    }

    @Test
    @DisplayName("replace_??null_?먮뒗_鍮??뗭씠硫??꾨Т寃껊룄_?섏젙?섏?_?딅뒗??")
    void replace_nullOrEmpty_doesNothing() {
        // given
        UserPosition pos = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        Set<UserPosition> previousPositions = new HashSet<>(Set.of(pos));
        userPositionAdapter.saveAll(previousPositions);

        // when
        userPositionAdapter.replace(previousPositions, null);
        // then
        Set<UserPosition> finalPositions1 = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(finalPositions1).hasSize(1)
                .extracting(UserPosition::positionCd)
                .containsExactly(TEST_POSITION_CD);

        // when
        userPositionAdapter.replace(previousPositions, new HashSet<>());

        // then
        Set<UserPosition> finalPositions2 = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(finalPositions2).hasSize(1)
                .extracting(UserPosition::positionCd)
                .containsExactly(TEST_POSITION_CD);
    }
}
