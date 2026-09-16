package teamdevhub.devhub.web.api.user.model.request;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.web.api.user.model.UpdateProfileRequestDto;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileCommand;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateProfileRequestDtoTest {

    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String NEW_USERNAME = "NewUsername";
    private static final String NEW_INTRO = "NewIntro";
    private static final List<String> NEW_POSITION_LIST = List.of("002");
    private static final List<String> NEW_SKILL_LIST = List.of("002");

    @Test
    @DisplayName("紐⑤뱺_媛믪씠_議댁옱?섎㈃_UpdateProfileCommand_濡?蹂?섎맂??")
    void convertAllValuesToCommand() {
        // given
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .positionList(NEW_POSITION_LIST)
                .skillList(NEW_SKILL_LIST)
                .build();

        // when
        UpdateProfileCommand updateProfileCommand = updateProfileRequestDto.toUpdateProfileCommand(TEST_USER_GUID_1);

        // then
        assertThat(updateProfileCommand.username()).isEqualTo(NEW_USERNAME);
        assertThat(updateProfileCommand.introduction()).isEqualTo(NEW_INTRO);
        assertThat(updateProfileCommand.positions()).hasSize(NEW_POSITION_LIST.size())
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrderElementsOf(NEW_POSITION_LIST);
        assertThat(updateProfileCommand.skills()).hasSize(NEW_SKILL_LIST.size())
                .extracting(UserSkill::skillCd)
                .containsExactlyInAnyOrderElementsOf(NEW_SKILL_LIST);
    }

    @Test
    @DisplayName("positionList_媛_null_?대㈃_positions_??null_?대떎")
    void positionsIsNullWhenPositionListIsNull() {
        // given
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .positionList(null)
                .build();

        // when
        UpdateProfileCommand updateProfileCommand = updateProfileRequestDto.toUpdateProfileCommand(TEST_USER_GUID_1);

        // then
        assertThat(updateProfileCommand.positions()).isNull();
    }

    @Test
    @DisplayName("skillList_媛_null_?대㈃_skills_??null_?대떎")
    void skillsIsNullWhenSkillListIsNull() {
        // given
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .introduction(NEW_INTRO)
                .skillList(null)
                .build();

        // when
        UpdateProfileCommand updateProfileCommand = updateProfileRequestDto.toUpdateProfileCommand(TEST_USER_GUID_1);

        // then
        assertThat(updateProfileCommand.skills()).isNull();
    }

    @Test
    @DisplayName("positionList_媛_鍮덇컪?대㈃_positions_??鍮?吏묓빀?쇰줈_蹂?섎맂??")
    void positionsIsEmptyWhenPositionListIsEmpty() {
        // given
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .positionList(List.of())
                .build();

        // when
        UpdateProfileCommand updateProfileCommand = updateProfileRequestDto.toUpdateProfileCommand(TEST_USER_GUID_1);

        // then
        assertThat(updateProfileCommand.positions()).isEmpty();
    }

    @Test
    @DisplayName("skillList_媛_鍮덇컪?대㈃_skills_??鍮?吏묓빀?쇰줈_蹂?섎맂??")
    void skillsIsEmptyWhenSkillListIsEmpty() {
        // given
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .skillList(List.of())
                .build();

        // when
        UpdateProfileCommand updateProfileCommand = updateProfileRequestDto.toUpdateProfileCommand(TEST_USER_GUID_1);

        // then
        assertThat(updateProfileCommand.skills()).isEmpty();
    }
}
