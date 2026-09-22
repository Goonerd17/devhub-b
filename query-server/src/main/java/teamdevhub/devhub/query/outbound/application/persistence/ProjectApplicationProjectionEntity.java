package teamdevhub.devhub.query.outbound.application.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "query_project_application_projection")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectApplicationProjectionEntity {
    @Id
    private String applicationGuid;

    @Column(nullable = false, length = 120)
    private String projectGuid;

    @Column(nullable = false, length = 120)
    private String applicantGuid;

    @Column(nullable = false, length = 20)
    private String statusCode;

    @Column(length = 120)
    private String approverGuid;

    @Column(nullable = false, unique = true, length = 120)
    private String lastEventId;

    @Column(nullable = false)
    private Instant lastEventAt;

    private ProjectApplicationProjectionEntity(String applicationGuid, String projectGuid, String applicantGuid,
            String statusCode, String approverGuid, String lastEventId, Instant lastEventAt) {
        this.applicationGuid = applicationGuid;
        this.projectGuid = projectGuid;
        this.applicantGuid = applicantGuid;
        this.statusCode = statusCode;
        this.approverGuid = approverGuid;
        this.lastEventId = lastEventId;
        this.lastEventAt = lastEventAt;
    }

    public static ProjectApplicationProjectionEntity submitted(String applicationGuid, String projectGuid,
            String applicantGuid, String eventId, Instant eventAt) {
        return new ProjectApplicationProjectionEntity(applicationGuid, projectGuid, applicantGuid,
                "3301", null, eventId, eventAt);
    }

    public void statusChanged(String statusCode, String approverGuid, String eventId, Instant eventAt) {
        this.statusCode = statusCode;
        this.approverGuid = approverGuid;
        this.lastEventId = eventId;
        this.lastEventAt = eventAt;
    }

    public void cancelled(String eventId, Instant eventAt) {
        this.statusCode = "CANCELLED";
        this.lastEventId = eventId;
        this.lastEventAt = eventAt;
    }
}
