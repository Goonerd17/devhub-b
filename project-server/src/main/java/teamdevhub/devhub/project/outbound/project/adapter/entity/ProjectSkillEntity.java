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
        name = "project_skill",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_project_skill_guid",
                        columnNames = "projectSkillGuid"
                )
        }
)
public class ProjectSkillEntity {

	@Id
	@Column(name="project_skill_guid", length = 32, nullable = false)
	private String projectSkillGuid;
	
	@Column(name="project_guid", length = 32, nullable = false)
    private String projectGuid;
	
	@Column(name = "skill_cd", length = 10, nullable = false)
    private String skillCd;
}
