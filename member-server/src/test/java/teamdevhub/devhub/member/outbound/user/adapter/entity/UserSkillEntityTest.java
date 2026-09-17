package teamdevhub.devhub.member.outbound.user.adapter.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.member.outbound.user.adapter.entity.UserSkillEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserSkillEntityTest {

    private static final String TEST_SKILL_CD = "001";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_USER_SKILL_GUID = "SKILL1a1b2c3d4e5f6g7h8i9j10k11l";

    @Test
    @DisplayName("UserSkillEntity_를_생성하고_getter_를_사용할_수_있다")
    void createEntityAndUseGetter() {
        // given
        UserSkillEntity userSkillEntity = UserSkillEntity.builder()
                .userSkillGuid(TEST_USER_SKILL_GUID)
                .userGuid(TEST_USER_GUID_1)
                .skillCd(TEST_SKILL_CD)
                .build();

        // then
        assertThat(userSkillEntity.getUserSkillGuid()).isEqualTo(TEST_USER_SKILL_GUID);
        assertThat(userSkillEntity.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userSkillEntity.getSkillCd()).isEqualTo(TEST_SKILL_CD);
    }
}
