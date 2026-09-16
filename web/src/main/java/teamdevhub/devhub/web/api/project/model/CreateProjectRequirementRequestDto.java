package teamdevhub.devhub.web.api.project.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.project.core.project.port.in.command.CreateProjectRequirementRequestCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectRequirementRequestDto {

    @NotBlank(message = "포지션 코드는 필수입니다")
    private String position;

    @NotBlank(message = "숙련도 코드는 필수입니다")
    private String level;

    @NotNull(message = "모집 인원은 필수입니다")
    @Min(value = 1, message = "모집 인원은 1명 이상이어야 합니다")
    private int capacity;

    public CreateProjectRequirementRequestCommand toCommand() {
        return CreateProjectRequirementRequestCommand.builder()
                .position(this.position)
                .level(this.level)
                .capacity(this.capacity)
                .build();
    }
}
