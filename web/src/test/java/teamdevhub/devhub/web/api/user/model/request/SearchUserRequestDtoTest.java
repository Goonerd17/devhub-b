package teamdevhub.devhub.web.api.user.model.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.web.api.user.model.SearchUserRequestDto;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SearchUserRequestDtoTest {

    @Test
    @DisplayName("blocked_媛_Y_?대㈃_blocked_??true_濡?蹂?섎맂??")
    void blockedYConvertsToTrue() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked("Y")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.blocked()).isTrue();
    }

    @Test
    @DisplayName("blocked_媛_N_?대㈃_blocked_??false_濡?蹂?섎맂??")
    void blockedNConvertsToFalse() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked("N")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.blocked()).isFalse();
    }

    @Test
    @DisplayName("blocked_媛_null_?대㈃_blocked_??null_?대떎")
    void blockedIsNullWhenBlockedIsNull() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked(null)
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.blocked()).isNull();
    }

    @Test
    @DisplayName("blocked_媛_Y_N_?댁쇅??媛믪씠硫?blocked_??null_?대떎")
    void blockedIsNullWhenBlockedIsInvalid() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked("X")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.blocked()).isNull();
    }

    @Test
    @DisplayName("keyword_媛_null_?대㈃_keyword_??null_?대떎")
    void keywordIsNullWhenKeywordIsNull() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .username(null)
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.username()).isNull();
    }

    @Test
    @DisplayName("keyword_媛_鍮덇컪?대㈃_keyword_??null_?대떎")
    void keywordIsNullWhenKeywordIsBlank() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .username("   ")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.username()).isNull();
    }

    @Test
    @DisplayName("keyword_??怨듬갚???덉쑝硫?trim_?섏뼱_蹂?섎맂??")
    void keywordIsTrimmed() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .username("  hello world  ")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.username()).isEqualTo("hello world");
    }

    @Test
    @DisplayName("joinedFrom_怨?joinedTo_??洹몃?濡??꾨떖?쒕떎")
    void joinedFromAndJoinedToArePassedThrough() {
        // given
        LocalDate joinedFrom = LocalDate.of(2024, 1, 1);
        LocalDate joinedTo = LocalDate.of(2024, 12, 31);

        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .joinedFrom(joinedFrom)
                .joinedTo(joinedTo)
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.joinedFrom())
                .isEqualTo(joinedFrom.atStartOfDay());

        assertThat(searchUserCommand.joinedTo())
                .isEqualTo(joinedTo.atTime(LocalTime.MAX));
    }
}
