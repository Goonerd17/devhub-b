package teamdevhub.devhub.web.api.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.member.core.user.port.in.command.AdminUpdateUserCommand;

@Schema(description = "관리자 사용자 정보 수정 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateUserRequestDto {

    @Schema(description = "닉네임", example = "newNickname")
    private String username;

    @Schema(description = "자기소개", example = "안녕하세요.")
    private String introduction;

    public AdminUpdateUserCommand toAdminUpdateUserCommand(String userGuid) {
        return AdminUpdateUserCommand.builder()
                .userGuid(userGuid)
                .username(this.username)
                .introduction(this.introduction)
                .build();
    }
}
