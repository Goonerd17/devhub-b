package teamdevhub.devhub.web.api.user.model;

import lombok.*;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserRequestDto {

    private String blocked;
    private LocalDate joinedFrom;
    private LocalDate joinedTo;
    private String username;

    public SearchUserCommand toSearchUserCommand() {
        Boolean blocked = null;
        if ("Y".equalsIgnoreCase(this.blocked)) {
            blocked = Boolean.TRUE;
        }

        if ("N".equalsIgnoreCase(this.blocked)) {
            blocked = Boolean.FALSE;
        }

        String username = null;
        if (this.username != null && !this.username.isBlank()) {
            username = this.username.trim();
        }

        LocalDateTime joinedFromDateTime = null;
        if (this.joinedFrom != null) {
            joinedFromDateTime = this.joinedFrom.atStartOfDay();
        }

        LocalDateTime joinedToDateTime = null;
        if (this.joinedTo != null) {
            joinedToDateTime = this.joinedTo.atTime(LocalTime.MAX);
        }

        return SearchUserCommand.builder()
                .blocked(blocked)
                .joinedFrom(joinedFromDateTime)
                .joinedTo(joinedToDateTime)
                .username(username)
                .build();
    }
}
