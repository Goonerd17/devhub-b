package teamdevhub.devhub.query.outbound.project.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "query_project_projection")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectProjectionEntity {
    @Id
    private String projectGuid;

    @Column(length = 120)
    private String ownerGuid;

    @Column(length = 300)
    private String title;

    @Column(length = 80)
    private String category;

    @Column(length = 120)
    private String username;

    @Column(length = 120)
    private String imageFileGuid;

    private LocalDate recruitmentStartDate;
    private LocalDate recruitmentEndDate;
    private LocalDateTime registeredDate;
    private boolean capacityClosed;

    @Column(length = 30)
    private String recruitStatus;

    @Column(nullable = false, length = 30)
    private String lifecycleStatus;

    @Column(nullable = false, unique = true, length = 120)
    private String lastEventId;

    @Column(nullable = false)
    private Instant lastEventAt;

    private ProjectProjectionEntity(String projectGuid, String ownerGuid, String title,
            String category, String username, String imageFileGuid, LocalDate recruitmentStartDate,
            LocalDate recruitmentEndDate, LocalDateTime registeredDate, boolean capacityClosed,
            String recruitStatus, String lifecycleStatus, String lastEventId, Instant lastEventAt) {
        this.projectGuid = projectGuid;
        this.ownerGuid = ownerGuid;
        this.title = title;
        this.category = category;
        this.username = username;
        this.imageFileGuid = imageFileGuid;
        this.recruitmentStartDate = recruitmentStartDate;
        this.recruitmentEndDate = recruitmentEndDate;
        this.registeredDate = registeredDate;
        this.capacityClosed = capacityClosed;
        this.recruitStatus = recruitStatus;
        this.lifecycleStatus = lifecycleStatus;
        this.lastEventId = lastEventId;
        this.lastEventAt = lastEventAt;
    }

    public static ProjectProjectionEntity created(String projectGuid, String ownerGuid, String title,
            String category, String username, String imageFileGuid, LocalDate recruitmentStartDate,
            LocalDate recruitmentEndDate, LocalDateTime registeredDate, boolean capacityClosed,
            String recruitStatus, String eventId, Instant eventAt) {
        return new ProjectProjectionEntity(projectGuid, ownerGuid, title, category, username, imageFileGuid,
                recruitmentStartDate, recruitmentEndDate, registeredDate, capacityClosed, recruitStatus,
                "CREATED", eventId, eventAt);
    }

    public void applyUpdate(String title, String category, String username, String imageFileGuid,
            LocalDate recruitmentStartDate, LocalDate recruitmentEndDate, boolean capacityClosed,
            String recruitStatus, String eventId, Instant eventAt) {
        this.title = title;
        this.category = category;
        this.username = username;
        this.imageFileGuid = imageFileGuid;
        this.recruitmentStartDate = recruitmentStartDate;
        this.recruitmentEndDate = recruitmentEndDate;
        this.capacityClosed = capacityClosed;
        this.recruitStatus = recruitStatus;
        this.lifecycleStatus = "UPDATED";
        this.lastEventId = eventId;
        this.lastEventAt = eventAt;
    }

    public void applyClosed(String eventId, Instant eventAt) {
        this.lifecycleStatus = "CLOSED";
        this.lastEventId = eventId;
        this.lastEventAt = eventAt;
    }
}
