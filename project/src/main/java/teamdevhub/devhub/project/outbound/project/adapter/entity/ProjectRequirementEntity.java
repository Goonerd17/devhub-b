package teamdevhub.devhub.project.outbound.project.adapter.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "project_requirement",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_project_requirement_guid",
                        columnNames = "projectRequirementGuid"
                )
        }
)
public class ProjectRequirementEntity {
	
	@Id
	@Column(name="project_requirement_guid", length = 32, nullable = false)
	private String projectRequirementGuid;
	
	@Column(name="project_guid", length = 32, nullable = false)
    private String projectGuid;
	
	@Column(name = "position_cd", length = 10, nullable = false)
    private String positionCd;
	
	@Column(name = "level_cd", length = 10 , nullable = false)
    private String levelCd;
	
	@Column(name = "capacity", nullable = false)
    private int capacity;
}
