package teamdevhub.devhub.community.http.report.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamdevhub.devhub.community.core.report.port.in.command.CreateReportCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReportRequestDto {

    private String boardGuid;

    private String commentGuid;

    @NotBlank(message = "카테고리 설정은 필수입니다")
    private String categoryCd;

    @NotBlank(message = "상세 사유는 필수입니다")
    private String reason;

    public CreateReportCommand toCommand(String reporterUser) {
        return CreateReportCommand.builder()
                .boardGuid(this.boardGuid)
                .commentGuid(this.commentGuid)
                .reporterUser(reporterUser)
                .categoryCd(this.categoryCd)
                .reason(this.reason)
                .build();
    }
}

