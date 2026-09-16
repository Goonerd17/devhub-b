package teamdevhub.devhub.project.outbound.project.adapter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "project_like",
        uniqueConstraints = {
        		@UniqueConstraint(
                        name = "uk_project_like_guid",
                        columnNames = "projectLikeGuid"
                )
        }
)
public class ProjectLikeEntity {
	
	@Id
	@Column(name="project_like_guid", length = 32, nullable = false)
	private String projectLikeGuid;
	
	@Column(name="project_guid", length = 32, nullable = false)
    private String projectGuid;
	
	@Column(name = "user_guid", length = 32, nullable = false)
    private String userGuid;
}
