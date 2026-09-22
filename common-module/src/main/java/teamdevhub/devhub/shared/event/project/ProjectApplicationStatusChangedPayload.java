package teamdevhub.devhub.shared.event.project;

public record ProjectApplicationStatusChangedPayload(
        String applicationGuid,
        String projectGuid,
        String applicantGuid,
        String statusCode,
        String approverGuid
) {
}
