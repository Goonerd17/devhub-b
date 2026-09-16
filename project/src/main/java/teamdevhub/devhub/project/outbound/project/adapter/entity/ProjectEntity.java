package teamdevhub.devhub.project.outbound.project.adapter.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.platform.outbound.common.persistence.jpa.audit.BaseEntity;
import teamdevhub.devhub.platform.outbound.common.persistence.jpa.converter.BooleanToYNConverter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "project",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_project_guid",
                        columnNames = "projectGuid"
                )
        }
)
public class ProjectEntity extends BaseEntity {
	
	@Id
	@Column(length = 32, nullable = false, unique = true)
    private String projectGuid;

    @Column(name = "attachment_file_guid", length = 32)
    private String attachmentFileGuid;
    
    @Column(name = "image_file_guid", length = 32)
    private String imageFileGuid;

    @Column( name = "user_guid", length = 32, nullable = false)
    private String userGuid;
    
    @Column(name = "recruitment_type_cd", length = 10, nullable = false)
    private String recruitmentTypeCd;
    
    @Column(name = "progress_type_cd", length = 10, nullable = false)
    private String progressTypeCd;
    
    @Column(name = "progress_region_cd", length = 10)
    private String progressRegionCd;
    
    @Column(name = "username", nullable = false)
    private String username;
    
    @Column(name = "category", nullable = false)
    private String category;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "content", nullable = false, length = 3000)
    private String content;
    
    @Column(name = "recruitment_start_date", nullable = false)
    private LocalDate recruitmentStartDate;
    
    @Column(name = "recruitment_end_date", nullable = false)
    private LocalDate recruitmentEndDate;
    
    @Column(name = "progress_start_date", nullable = false)
    private LocalDate progressStartDate;
    
    @Column(name = "progress_end_date", nullable = false)
    private LocalDate progressEndDate;
    
    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "delete_yn", nullable = false)
    private boolean deleted;
    
    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "capacity_closed_yn", nullable = false)
    private boolean capacityClosed;
}
