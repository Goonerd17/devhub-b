package teamdevhub.devhub.member.outbound.user.adapter.entity;

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
import teamdevhub.devhub.platform.outbound.common.persistence.jpa.audit.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "user_review",
        uniqueConstraints = {
        		@UniqueConstraint(
                        name = "uk_user_review_guid",
                        columnNames = "userReviewGuid"
                )
        }
)
public class UserReviewEntity extends BaseEntity {
	
	@Id
	@Column(name="user_review_guid", length = 32, nullable = false)
	private String userReviewGuid;
	
	@Column(name="project_guid", length = 32, nullable = false)
    private String projectGuid;
	
	@Column(name = "reviewer", nullable = false)
    private String reviewer;
	
	@Column(name = "reviewee", nullable = false)
    private String reviewee;
	
	@Column(name = "score", nullable = false)
    private double score;
}
