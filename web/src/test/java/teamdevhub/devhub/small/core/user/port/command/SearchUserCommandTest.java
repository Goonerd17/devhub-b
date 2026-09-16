package teamdevhub.devhub.web.core.user.port.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.web.api.user.model.SearchUserRequestDto;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SearchUserCommandTest {

    @Test
    @DisplayName("blocked_媛_Y_?대㈃_true_濡?蹂?섎맂??")
    void returnTrueWhenBlockedIsY() {
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
    @DisplayName("blocked_媛_N_?대㈃_false_濡?蹂?섎맂??")
    void returnFalseWhenBlockedIsN() {
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
    @DisplayName("blocked_媛_Y_?대㈃_true_濡?蹂?섎맂??")
    void returnNullWhenBlockedIsNull() {
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
    @DisplayName("keyword_媛_blank_?대㈃_null_濡?蹂?섎맂??")
    void returnNullWhenKeywordIsBlank() {
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
    @DisplayName("keyword_媛_議댁옱?섎㈃_trim_?섏뼱_?ㅼ젙?쒕떎")
    void trimKeywordWhenItExists() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .username("  hello  ")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.username()).isEqualTo("hello");
    }

    @Test
    @DisplayName("joinedFrom_怨?joinedTo_??洹몃?濡??꾨떖?쒕떎")
    void preserveJoinedFromAndJoinedToValues() {
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

    @Test
    @DisplayName("blocked_????뚮Ц??援щ텇?놁씠_?숈옉?쒕떎")
    void shouldHandleBlockedCaseInsensitively() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked("y")
                .build();

        // when
        SearchUserCommand searchUserCommand = searchUserRequestDto.toSearchUserCommand();

        // then
        assertThat(searchUserCommand.blocked()).isTrue();
    }
}
