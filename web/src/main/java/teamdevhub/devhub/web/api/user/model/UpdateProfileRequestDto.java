package teamdevhub.devhub.web.api.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileCommand;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(description = "사용자 프로필 수정 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequestDto {

    @Schema(description = "닉네임", example = "newDevHunter")
    private String username;

    @Schema(description = "자기소개", example = "안녕하세요, 풀스택 개발자입니다.")
    private String introduction;

    @Schema(description = "관심 포지션 코드 목록", example = "[\"백엔드\", \"DevOps\"]")
    private List<String> positionList;

    @Schema(description = "기술 스택 코드 목록", example = "[\"Java\", \"Kubernetes\"]")
    private List<String> skillList;

    public UpdateProfileCommand toUpdateProfileCommand(String userGuid) {
        return UpdateProfileCommand.builder()
                .userGuid(userGuid)
                .username(this.username)
                .introduction(this.introduction)
                .positions(toPositions(this.positionList, userGuid))
                .skills(toSkills(this.skillList, userGuid))
                .build();
    }

    private static Set<UserPosition> toPositions(List<String> positionList, String userGuid) {
        if (positionList == null) {
            return null;
        }
        return positionList.stream()
                .map(positionCd -> new UserPosition(userGuid, positionCd))
                .collect(Collectors.toSet());
    }

    private static Set<UserSkill> toSkills(List<String> skillList, String userGuid) {
        if (skillList == null) {
            return null;
        }
        return skillList.stream()
                .map(skillCd -> new UserSkill(userGuid, skillCd))
                .collect(Collectors.toSet());
    }
}
