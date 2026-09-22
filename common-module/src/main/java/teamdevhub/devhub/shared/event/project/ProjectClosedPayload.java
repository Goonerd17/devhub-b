package teamdevhub.devhub.shared.event.project;

public record ProjectClosedPayload(String projectGuid, String ownerGuid, String title) {
}
