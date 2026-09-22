package teamdevhub.devhub.shared.event.project;

public record ProjectApplicationCancelledPayload(
        String applicationGuid,
        String projectGuid,
        String applicantGuid
) {
}
