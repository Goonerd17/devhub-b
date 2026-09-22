package teamdevhub.devhub.admin.http.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.shared.member.AdminMemberView;

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

    public static AdminUserDetailResponseDto fromView(AdminMemberView user) {
        return AdminUserDetailResponseDto.builder()
                .userGuid(user.userGuid()).username(user.username()).introduction(user.introduction())
                .fileGuid(user.fileGuid()).userRole(user.userRole()).mannerDegree(user.mannerDegree())
                .blocked(user.blocked()).blockEndDate(user.blockEndDate()).deleted(user.deleted())
                .positionList(user.positions()).skillList(user.skills())
                .registeredDate(user.registeredDate()).modifiedDate(user.modifiedDate())
                .build();
    }
}
