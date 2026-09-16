package teamdevhub.devhub.web.api.project.model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.web.api.admin.form.model.CreateApplicationFormRequestDto;
import teamdevhub.devhub.administration.api.ApplicationFormDefinition;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectCommand;

@Schema(description = "프로젝트 생성 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectRequestDto {

    @Schema(description = "프로젝트 제목", example = "Spring Boot 포트폴리오 프로젝트")
    @NotBlank(message = "제목은 필수입니다")
    private String title;

    @Schema(description = "카테고리 코드", example = "PROJECT")
    @NotBlank(message = "카테고리는 필수입니다")
    private String category;

    @Schema(description = "프로젝트 상세 내용", example = "Spring Boot + React 웹 서비스를 함께 개발할 팀원을 모집합니다.")
    @NotBlank(message = "상세내용은 필수입니다")
    @Size(max = 3000, message = "상세내용은 3000자를 초과할 수 없습니다")
    private String content;

    @Schema(description = "첨부 파일 GUID", example = "abc123def456ghi789jkl012")
    private String attachmentFileGuid;

    @Schema(description = "대표 이미지 파일 GUID", example = "img123abc456def789ghi012")
    private String imageFileGuid;

    @Schema(description = "모집 방식 코드", example = "ONLINE")
    @NotBlank(message = "모집방식은 필수입니다")
    private String recruitmentTypeCd;

    @Schema(description = "진행 방식 코드", example = "REMOTE")
    @NotBlank(message = "진행방식은 필수입니다")
    private String progressTypeCd;

    @Schema(description = "진행 지역 코드 (진행방식이 오프라인일 경우)", example = "SEOUL")
    private String progressRegionCd;

    @Schema(description = "모집 시작일 (yyyy-MM-dd)", example = "2025-05-01")
    @NotNull(message = "모집시작일은 필수입니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentStartDate;

    @Schema(description = "모집 종료일 (yyyy-MM-dd)", example = "2025-05-31")
    @NotNull(message = "모집종료일은 필수입니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentEndDate;

    @Schema(description = "진행 시작일 (yyyy-MM-dd)", example = "2025-06-01")
    @NotNull(message = "진행시작일은 필수입니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate progressStartDate;

    @Schema(description = "진행 종료일 (yyyy-MM-dd)", example = "2025-08-31")
    @NotNull(message = "진행종료일은 필수입니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate progressEndDate;

    @Schema(description = "기술 스택 목록 (최소 1개)", example = "[\"Java\", \"Spring\", \"React\"]")
    @NotNull(message = "기술스택은 필수입니다")
    @Size(min = 1, message = "기술스택은 최소 1개 이상 선택해야 합니다")
    private List<String> skillList;

    @Schema(description = "모집 포지션 및 인원 목록 (최소 1개)")
    @NotNull(message = "모집인원은 필수입니다")
    @Size(min = 1, message = "모집인원은 최소 1개 이상 선택해야 합니다")
    private List<CreateProjectRequirementRequestDto> positionList;

    @Schema(description = "사용할 지원서 양식 GUID 목록 (최소 1개)")
    @NotNull(message = "신청양식은 필수입니다")
    @Size(min = 1, message = "신청양식은 최소 1개 이상 선택해야 합니다")
    private List<String> applicationFormList;

    @Schema(description = "추가 지원서 양식 항목 목록 (선택)")
    private List<CreateApplicationFormRequestDto> additionalFormList;
    
    public CreateProjectCommand toCommand(String userGuid) {
        List<ApplicationFormDefinition> additionalFormCommands = null;

        if (this.additionalFormList != null) {
            additionalFormCommands = this.additionalFormList.stream()
                    .map(CreateApplicationFormRequestDto::toDefinition)
                    .toList();
        }

    	return CreateProjectCommand.builder()
    			.userGuid(userGuid)
    			.title(this.title)
    			.category(this.category)
    			.content(this.content)
    			.attachmentFileGuid(this.attachmentFileGuid)
    			.imageFileGuid(this.imageFileGuid)
    			.recruitmentTypeCd(this.recruitmentTypeCd)
    			.progressTypeCd(this.progressTypeCd)
    			.progressRegionCd(this.progressRegionCd)
    			.recruitmentStartDate(this.recruitmentStartDate)
    			.recruitmentEndDate(this.recruitmentEndDate)
    			.progressStartDate(this.progressStartDate)
    			.progressEndDate(this.progressEndDate)
    			.skillList(this.skillList)
                .positionList(
                        this.positionList.stream()
                                .map(CreateProjectRequirementRequestDto::toCommand)
                                .collect(Collectors.toList())
                )
    			.applicationFormList(this.applicationFormList)
                .additionalFormList(additionalFormCommands)
    			.build();
    }

}
