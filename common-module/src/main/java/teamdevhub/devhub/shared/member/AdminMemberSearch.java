package teamdevhub.devhub.shared.member;

import java.time.LocalDateTime;

public record AdminMemberSearch(Boolean blocked, LocalDateTime joinedFrom, LocalDateTime joinedTo, String username) {
}
