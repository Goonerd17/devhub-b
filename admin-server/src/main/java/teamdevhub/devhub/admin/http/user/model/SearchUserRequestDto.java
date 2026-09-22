package teamdevhub.devhub.admin.http.user.model;

import lombok.*;
import teamdevhub.devhub.shared.member.AdminMemberSearch;

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

    public AdminMemberSearch toSearchUserCommand() {
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

        return new AdminMemberSearch(blocked, joinedFromDateTime, joinedToDateTime, username);
    }
}
