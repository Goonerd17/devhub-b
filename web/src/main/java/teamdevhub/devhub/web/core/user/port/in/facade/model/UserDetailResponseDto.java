package teamdevhub.devhub.web.core.user.port.in.facade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponseDto {

    private UserBasicResponseDto user;
    private List<String> positionList;
    private List<String> skillList;
    private boolean passwordLoginAvailable;

    public static UserDetailResponseDto fromDomain(User user, boolean passwordLoginAvailable) {
        return UserDetailResponseDto.builder()
                .user(UserBasicResponseDto.fromDomain(user))
                .positionList(positionList(user.getPositions()))
                .skillList(skillList(user.getSkills()))
                .passwordLoginAvailable(passwordLoginAvailable)
                .build();
    }

    public static List<String> positionList(Set<UserPosition> userPositions) {
        return userPositions.stream()
                .map(UserPosition::positionCd)
                .toList();
    }

    public static List<String> skillList(Set<UserSkill> userSkills) {
        return userSkills.stream()
                .map(UserSkill::skillCd)
                .toList();
    }
}
