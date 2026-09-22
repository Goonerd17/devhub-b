package teamdevhub.devhub.shared.event.project;

public record ProjectApplicationSubmittedPayload(
        String projectGuid,
        String applicationGuid,
        String requirementGuid,
        String applicantGuid
) {
}
