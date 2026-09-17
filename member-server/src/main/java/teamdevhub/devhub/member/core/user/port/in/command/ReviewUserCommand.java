package teamdevhub.devhub.member.core.user.port.in.command;

import lombok.Builder;

@Builder
public record ReviewUserCommand(
        String userReviewGuid,
        String projectGuid,
        String reviewerGuid,
        String revieweeGuid,
        double score
) {}
