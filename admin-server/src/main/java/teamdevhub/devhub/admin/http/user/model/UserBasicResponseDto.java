package teamdevhub.devhub.admin.http.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.shared.member.AdminMemberView;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
public class UserBasicResponseDto {

    private String userGuid;
    private String username;
    private String userRole;
    private String introduction;

    private String fileGuid;

    private double mannerDegree;

    private boolean blocked;
    private LocalDateTime blockEndDate;

    private boolean deleted;

    private LocalDateTime lastLoginDateTime;

    private String registrantGuid;
    private LocalDateTime registeredDate;
    private String modifierGuid;
    private LocalDateTime modifiedDate;

    public static UserBasicResponseDto fromView(AdminMemberView user) {

        return UserBasicResponseDto.builder()
                .userGuid(user.userGuid()).username(user.username()).userRole(user.userRole())
                .introduction(user.introduction()).fileGuid(user.fileGuid()).mannerDegree(user.mannerDegree())
                .blocked(user.blocked()).blockEndDate(user.blockEndDate()).deleted(user.deleted())
                .lastLoginDateTime(user.lastLoginDateTime()).registrantGuid(user.registrantGuid())
                .registeredDate(user.registeredDate()).modifierGuid(user.modifierGuid()).modifiedDate(user.modifiedDate())
                .build();
    }
}
