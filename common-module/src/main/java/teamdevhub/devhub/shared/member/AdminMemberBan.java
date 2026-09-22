package teamdevhub.devhub.shared.member;

import java.time.LocalDateTime;

public record AdminMemberBan(String userGuid, LocalDateTime blockEndDate) {
}
