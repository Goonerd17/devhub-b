package teamdevhub.devhub.admin.core.terms.port.in.command;

import lombok.Builder;

@Builder
public record CreateTermsCommand(
        String title,
        String content,
        boolean isRequired,
        boolean isUsed,
        boolean isDeleted
) {
}
