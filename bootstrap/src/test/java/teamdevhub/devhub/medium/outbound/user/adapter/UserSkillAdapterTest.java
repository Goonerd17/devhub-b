package teamdevhub.devhub.medium.outbound.user.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.member.outbound.user.adapter.UserSkillAdapter;
import teamdevhub.devhub.member.outbound.user.persistence.JpaUserSkillRepository;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@Transactional
class UserSkillAdapterTest {

    @Autowired
    private UserSkillAdapter userSkillAdapter;

    @Autowired
    private JpaUserSkillRepository jpaUserSkillRepository;

    @BeforeEach
    void init() {
        jpaUserSkillRepository.deleteAll();
    }

    @Test
    @DisplayName("?꾩껜 ?ㅽ궗????ν븳??")
    void saveAll_savesSkillsCorrectly() {
        // given
        UserSkill skill1 = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        UserSkill skill2 = new UserSkill(TEST_USER_GUID_1, NEW_SKILL_CD);
        Set<UserSkill> skills = new HashSet<>(Set.of(skill1, skill2));

        // when
        userSkillAdapter.saveAll(skills);

        // then
        Set<UserSkill> saved = userSkillAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(saved).hasSize(2)
                .extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrder(TEST_SKILL_CD, NEW_SKILL_CD);
    }

    @Test
    @DisplayName("saveAll_?_鍮??뗭씠硫??꾨Т寃껊룄_??ν븯吏_?딅뒗??")
    void saveAll_emptySet_doesNothing() {
        // given
        Set<UserSkill> emptySkills = new HashSet<>();

        // when
        userSkillAdapter.saveAll(emptySkills);

        // then
        Set<UserSkill> saved = userSkillAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(saved).isEmpty();
    }

    @Test
    @DisplayName("?ㅽ궗??蹂寃쎈릺硫?紐⑤뱺 蹂寃쎌궗??씠 諛섏쁺?쒕떎")
    void replace_mergesOldAndNewSkillsCorrectly() {
        // given
        UserSkill oldSkill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        Set<UserSkill> previousSkills = new HashSet<>(Set.of(oldSkill));
        userSkillAdapter.saveAll(previousSkills);

        UserSkill newSkill = new UserSkill(TEST_USER_GUID_1, NEW_SKILL_CD);
        Set<UserSkill> currentSkills = new HashSet<>(Set.of(oldSkill, newSkill));

        // when
        userSkillAdapter.replace(previousSkills, currentSkills);

        // then
        Set<UserSkill> finalSkills = userSkillAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(finalSkills).hasSize(2)
                .extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrder(TEST_SKILL_CD, NEW_SKILL_CD);
    }

    @Test
    @DisplayName("??젣???ㅽ궗媛믪? ?쒓굅?쒕떎")
    void replace_removesDeletedSkillsCorrectly() {
        // given
        UserSkill oldSkill1 = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        UserSkill oldSkill2 = new UserSkill(TEST_USER_GUID_1, NEW_SKILL_CD);
        Set<UserSkill> previousSkills = new HashSet<>(Set.of(oldSkill1, oldSkill2));
        userSkillAdapter.saveAll(previousSkills);

        Set<UserSkill> currentSkills = new HashSet<>(Set.of(oldSkill1));

        // when
        userSkillAdapter.replace(previousSkills, currentSkills);

        // then
        Set<UserSkill> finalSkills = userSkillAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(finalSkills).hasSize(1)
                .extracting(UserSkill::skillCd)
                .containsExactly(TEST_SKILL_CD);
    }

    @Test
    @DisplayName("蹂寃쎌궗??씠 ?놁쑝硫??ㅽ궗? 洹몃?濡??좎??쒕떎")
    void replace_noChanges_doesNothing() {
        // given
        UserSkill skill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        Set<UserSkill> skills = new HashSet<>(Set.of(skill));
        userSkillAdapter.saveAll(skills);

        // when
        userSkillAdapter.replace(skills, skills);

        // then
        Set<UserSkill> finalSkills = userSkillAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(finalSkills).hasSize(1)
                .extracting(UserSkill::skillCd)
                .containsExactly(TEST_SKILL_CD);
    }

    @Test
    @DisplayName("replace_媛_null_?먮뒗_鍮??뗭씠硫??꾨Т寃껊룄_?섏젙?섏?_?딅뒗??")
    void replace_nullOrEmpty_doesNothing() {
        // given
        UserSkill skill = new UserSkill(TEST_USER_GUID_1, TEST_SKILL_CD);
        Set<UserSkill> previousSkills = new HashSet<>(Set.of(skill));
        userSkillAdapter.saveAll(previousSkills);

        // when
        userSkillAdapter.replace(previousSkills, null);
        userSkillAdapter.replace(previousSkills, new HashSet<>());

        // then
        Set<UserSkill> finalSkills1 = userSkillAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(finalSkills1).hasSize(1)
                .extracting(UserSkill::skillCd)
                .containsExactly(TEST_SKILL_CD);

        Set<UserSkill> finalSkills2 = userSkillAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(finalSkills2).hasSize(1)
                .extracting(UserSkill::skillCd)
                .containsExactly(TEST_SKILL_CD);
    }
}
