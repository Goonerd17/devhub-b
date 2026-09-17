package teamdevhub.devhub.member.core.user.port.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileCommand;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateProfileCommandTest {

    private static final String NEW_USERNAME = "NewUsername";
    private static final String NEW_INTRO = "NewIntro";
    private static final java.util.Set<teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition> NEW_USER_POSITIONS =
            java.util.Set.of(new teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition("USER1", "002"));
    private static final java.util.Set<teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill> NEW_USER_SKILLS =
            java.util.Set.of(new teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill("USER1", "002"));

    @Test
    @DisplayName("username_이_존재하면_hasUsernameAndIntroductionChange_는_true_이다")
    void hasUsernameChangeIsTrue() {
        // given
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .username(NEW_USERNAME)
                .build();

        // when, then
        assertThat(updateProfileCommand.hasUsernameAndIntroductionChange()).isTrue();
    }

    @Test
    @DisplayName("introduction_이_존재하면_hasUsernameAndIntroductionChange_는_true_이다")
    void hasIntroductionChangeIsTrue() {
        // given
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .introduction(NEW_INTRO)
                .build();

        // when, then
        assertThat(updateProfileCommand.hasUsernameAndIntroductionChange()).isTrue();
    }

    @Test
    @DisplayName("username_과_introduction_이_모두_null_이면_hasUsernameAndIntroductionChange_는_false_이다")
    void hasUsernameAndIntroductionChangeIsFalse() {
        // given
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .build();

        // when, then
        assertThat(updateProfileCommand.hasUsernameAndIntroductionChange()).isFalse();
    }

    @Test
    @DisplayName("positions_가_null_이_아니면_hasPositionsChange_는_true_이다")
    void hasPositionsChangeIsTrue() {
        // given
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .positions(NEW_USER_POSITIONS)
                .build();

        // when, then
        assertThat(updateProfileCommand.hasPositionsChange()).isTrue();
    }

    @Test
    @DisplayName("positions_가_null_이면_hasPositionsChange_는_false_이다")
    void hasPositionsChangeIsFalse() {
        // given
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .positions(null)
                .build();

        // when, then
        assertThat(updateProfileCommand.hasPositionsChange()).isFalse();
    }

    @Test
    @DisplayName("skills_가_null_이_아니면_hasSkillsChange_는_true_이다")
    void hasSkillsChangeIsTrue() {
        // given
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .skills(NEW_USER_SKILLS)
                .build();

        // when, then
        assertThat(updateProfileCommand.hasSkillsChange()).isTrue();
    }

    @Test
    @DisplayName("skills_가_null_이면_hasSkillsChange_는_false_이다")
    void hasSkillsChangeIsFalse() {
        // given
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .skills(null)
                .build();

        // when, then
        assertThat(updateProfileCommand.hasSkillsChange()).isFalse();
    }
}
