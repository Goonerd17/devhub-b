package teamdevhub.devhub.web.api.project.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.member.core.user.port.in.command.ReviewUserCommand;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUserRequestDto {

    @NotNull(message = "리뷰 점수는 필수입니다")
    @DecimalMin(value = "1.0", message = "리뷰 점수는 최소 1.0 이상이어야 합니다")
    @DecimalMax(value = "5.0", message = "리뷰 점수는 최대 5.0 이하여야 합니다")
    private Double score;

    public ReviewUserCommand toReviewUserCommand(String projectGuid, String userGuid, ReviewUserRequestDto reviewUserRequestDto, AuthenticatedUser authenticatedUser) {
        return ReviewUserCommand.builder()
                .projectGuid(projectGuid)
                .reviewerGuid(authenticatedUser.userGuid())
                .revieweeGuid(userGuid)
                .score(reviewUserRequestDto.getScore())
                .build();

    }
}
