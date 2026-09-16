package teamdevhub.devhub.web.api.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Schema(description = "관리자용 사용자 상세 조회 응답")
@Getter
@Builder
public class AdminUserDetailResponseDto {

    private String userGuid;
    private String username;
    private String introduction;
    private String fileGuid;
    private String userRole;
    private double mannerDegree;
    private boolean blocked;
    private LocalDateTime blockEndDate;
    private boolean deleted;
    private List<String> positionList;
    private List<String> skillList;
    private LocalDateTime registeredDate;
    private LocalDateTime modifiedDate;

    public static AdminUserDetailResponseDto fromDomain(User user) {
        return AdminUserDetailResponseDto.builder()
                .userGuid(user.getUserGuid())
                .username(user.getUsername())
                .introduction(user.getIntroduction())
                .fileGuid(user.getFileGuid())
                .userRole(user.getUserRole().name())
                .mannerDegree(user.getMannerDegree())
                .blocked(user.isBlocked())
                .blockEndDate(user.getBlockEndDate())
                .deleted(user.isDeleted())
                .positionList(toPositionList(user.getPositions()))
                .skillList(toSkillList(user.getSkills()))
                .registeredDate(user.getAuditInfo().registeredDate())
                .modifiedDate(user.getAuditInfo().modifiedDate())
                .build();
    }

    private static List<String> toPositionList(Set<UserPosition> positions) {
        return positions.stream().map(UserPosition::positionCd).toList();
    }

    private static List<String> toSkillList(Set<UserSkill> skills) {
        return skills.stream().map(UserSkill::skillCd).toList();
    }
}
