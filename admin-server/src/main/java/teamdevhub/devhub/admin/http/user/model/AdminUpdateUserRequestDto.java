package teamdevhub.devhub.admin.http.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.member.AdminMemberUpdate;

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

    public AdminMemberUpdate toAdminUpdateUserCommand(String userGuid) {
        return new AdminMemberUpdate(userGuid, this.username, this.introduction);
    }
}
