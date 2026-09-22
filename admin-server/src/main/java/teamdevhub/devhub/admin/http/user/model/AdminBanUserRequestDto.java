package teamdevhub.devhub.admin.http.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.member.AdminMemberBan;

import java.time.LocalDateTime;

@Schema(description = "사용자 정지 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminBanUserRequestDto {

    @Schema(description = "정지 종료 일시 (null이면 영구 정지)", example = "2025-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime blockEndDate;

    public AdminMemberBan toBanUserCommand(String userGuid) {
        return new AdminMemberBan(userGuid, this.blockEndDate);
    }
}
