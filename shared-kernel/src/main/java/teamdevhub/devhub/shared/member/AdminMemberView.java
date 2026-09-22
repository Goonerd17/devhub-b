package teamdevhub.devhub.shared.member;

import java.time.LocalDateTime;
import java.util.List;

public record AdminMemberView(String userGuid, String username, String userRole, String introduction,
        String fileGuid, double mannerDegree, boolean blocked, LocalDateTime blockEndDate,
        boolean deleted, LocalDateTime lastLoginDateTime, List<String> positions, List<String> skills,
        String registrantGuid, LocalDateTime registeredDate, String modifierGuid, LocalDateTime modifiedDate) {
}
