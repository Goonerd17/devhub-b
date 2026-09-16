package teamdevhub.devhub.web.core.user.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.member.core.user.port.in.command.ReviewUserCommand;
import teamdevhub.devhub.web.core.user.port.in.facade.UserReviewFacade;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.project.FakeProjectMemberUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserProfileUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserReviewUseCase;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserReviewFacadeTest {

    private static final String TEST_PROJECT_GUID_1 = "PROJECT1a1b2c3d4e5f6g7h8i9j10k";
    private static final String TEST_REVIEW_GUID_1 = "REVIEW1a1b2c3d4e5f6g7h8i9j10k11";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_USER_GUID_2 = "USER2a1b2c3d4e5f6g7h8i9j10k11l12";

    private UserReviewFacade userReviewFacade;

    private FakeProjectMemberUseCase projectMemberUseCase;
    private FakeUserReviewUseCase userReviewUseCase;
    private FakeUserProfileUseCase userProfileUseCase;

    @BeforeEach
    void init() {
        projectMemberUseCase = new FakeProjectMemberUseCase();
        userReviewUseCase = new FakeUserReviewUseCase();
        userProfileUseCase = new FakeUserProfileUseCase();

        userReviewFacade = new UserReviewFacade(
                projectMemberUseCase,
                userReviewUseCase,
                userProfileUseCase
        );
    }

    private ReviewUserCommand command(double score) {
        return ReviewUserCommand.builder()
                .userReviewGuid(TEST_REVIEW_GUID_1)
                .projectGuid(TEST_PROJECT_GUID_1)
                .reviewerGuid(TEST_USER_GUID_1)
                .revieweeGuid(TEST_USER_GUID_2)
                .score(score)
                .build();
    }

    @Test
    @DisplayName("由щ럭_?붿껌???섎㈃_寃利???由щ럭媛_??λ릺怨?留ㅻ꼫?꾧?_?낅뜲?댄듃?쒕떎")
    void reviewMember_validCommand_delegatesCorrectly() {
        // given
        userReviewUseCase.willReturn(4.0);

        // when
        userReviewFacade.reviewMember(command(4.0));

        // then
        assertThat(projectMemberUseCase.called).isTrue();
        assertThat(userReviewUseCase.called).isTrue();
        assertThat(userProfileUseCase.called).isTrue();

        assertThat(userProfileUseCase.lastUserGuid).isEqualTo(TEST_USER_GUID_2);
        assertThat(userProfileUseCase.lastScore).isEqualTo(4.0);
    }

    @Test
    @DisplayName("寃利앹뿉???덉쇅媛_諛쒖깮?섎㈃_由щ럭?_留ㅻ꼫???낅뜲?댄듃???ㅽ뻾?섏?_?딅뒗??")
    void reviewMember_validationFails_stopsFlow() {
        // given
        projectMemberUseCase.willThrow(
                BusinessRuleException.of(ErrorCode.PROJECT_NOT_COMPLETED)
        );

        // when & then
        assertThatThrownBy(() ->
                userReviewFacade.reviewMember(command(4.0))
        ).isInstanceOf(BusinessRuleException.class);

        assertThat(userReviewUseCase.called).isFalse();
        assertThat(userProfileUseCase.called).isFalse();
    }
}
