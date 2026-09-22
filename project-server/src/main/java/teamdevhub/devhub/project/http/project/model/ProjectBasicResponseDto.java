package teamdevhub.devhub.project.http.project.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.project.core.project.domain.Project;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ProjectBasicResponseDto {
	
	private String projectGuid;
    private String userGuid;
    private String username;
    
    private String category;
    private String title;
    private String content;
    
    private String attachmentFileGuid;
	private String imageFileGuid;
    
    private String recruitmentTypeCd;
    private LocalDate recruitmentStartDate;
    private LocalDate recruitmentEndDate;
    
    private String progressTypeCd;
    private String progressRegionCd;
    private String progressPeriod;
    private LocalDate progressStartDate;
    private LocalDate progressEndDate;
    
    private String registrantGuid;
    private LocalDateTime registeredDate;
    private String modifierGuid;
    private LocalDateTime modifiedDate;
    
    public static ProjectBasicResponseDto fromDomain(Project project) {
    	ProjectBasicResponseDtoBuilder<?, ?> builder = ProjectBasicResponseDto.builder();
    	fillBase(builder, project);
        return builder.build();
    }
        
    protected static <B extends ProjectBasicResponseDtoBuilder<?, ?>> void fillBase(B builder, Project project) {
        builder
	        .projectGuid(project.getProjectGuid())
	        .userGuid(project.getUserGuid())
	        .username(project.getUsername())
	        .category(project.getCategory())
	        .title(project.getTitle())
	        .content(project.getContent())
	        .attachmentFileGuid(project.getAttachmentFileGuid())
	        .imageFileGuid(project.getImageFileGuid())
	        .recruitmentTypeCd(project.getRecruitmentTypeCd())
	        .recruitmentStartDate(project.getRecruitmentStartDate())
	        .recruitmentEndDate(project.getRecruitmentEndDate())
	        .progressTypeCd(project.getProgressTypeCd())
	        .progressRegionCd(project.getProgressRegionCd())
	        .progressPeriod(project.getProgressPeriod())
	        .progressStartDate(project.getProgressStartDate())
	        .progressEndDate(project.getProgressEndDate())
	        .registrantGuid(project.getAuditInfo().registrantGuid())
	        .registeredDate(project.getAuditInfo().registeredDate())
	        .modifierGuid(project.getAuditInfo().modifierGuid())
	        .modifiedDate(project.getAuditInfo().modifiedDate());
    }
    
}

