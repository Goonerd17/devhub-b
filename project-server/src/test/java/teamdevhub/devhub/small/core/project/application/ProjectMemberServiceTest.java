package teamdevhub.devhub.project.core.project.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.project.core.project.application.ProjectMemberService;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.fake.pure.application.port.out.project.FakeProjectMemberRepository;
import teamdevhub.devhub.fake.pure.application.port.out.project.FakeProjectRepository;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectMemberServiceTest {

    private static final String TEST_PROJECT_GUID_1 = "PRJT1a1b2c3d4e5f6g7h8i9j10k11l1";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_USER_GUID_2 = "USER2a1b2c3d4e5f6g7h8i9j10k11l12";

    private ProjectMemberService projectMemberService;

    private FakeProjectRepository projectRepository;
    private FakeProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void init() {
        projectRepository = new FakeProjectRepository();
        projectMemberRepository = new FakeProjectMemberRepository();

        projectMemberService = new ProjectMemberService(
                projectRepository,
                projectMemberRepository
        );
    }

    private Project completedProject() {
        return Project.builder()
                .projectGuid(TEST_PROJECT_GUID_1)
                .userGuid(TEST_USER_GUID_1)
                .progressEndDate(LocalDate.now().minusDays(1))
                .build();
    }

    private Project incompleteProject() {
        return Project.builder()
                .projectGuid(TEST_PROJECT_GUID_1)
                .userGuid(TEST_USER_GUID_1)
                .progressEndDate(LocalDate.now().plusDays(30))
                .build();
    }

    @Test
    @DisplayName("유효한_요청이면_리뷰_가능_검증을_통과한다")
    void validateReviewable_validRequest_passes() {
        // given
        projectRepository.save(completedProject());
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_1);
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_2);

        // when & then
        projectMemberService.validateReviewable(
                TEST_PROJECT_GUID_1,
                TEST_USER_GUID_1,
                TEST_USER_GUID_2
        );
    }

    @Test
    @DisplayName("프로젝트가_완료되지_않으면_예외가_발생한다")
    void validateReviewable_projectNotCompleted_throwsException() {
        // given
        projectRepository.save(incompleteProject());

        // when & then
        assertThatThrownBy(() ->
                projectMemberService.validateReviewable(
                        TEST_PROJECT_GUID_1,
                        TEST_USER_GUID_1,
                        TEST_USER_GUID_2
                )
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.PROJECT_NOT_COMPLETED.getMessage());
    }

    @Test
    @DisplayName("리뷰어가_프로젝트_멤버가_아니면_예외가_발생한다")
    void validateReviewable_reviewerNotMember_throwsException() {
        // given
        projectRepository.save(completedProject());
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_2);

        // when & then
        assertThatThrownBy(() ->
                projectMemberService.validateReviewable(
                        TEST_PROJECT_GUID_1,
                        TEST_USER_GUID_1,
                        TEST_USER_GUID_2
                )
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REVIEW_NOT_A_MEMBER.getMessage());
    }

    @Test
    @DisplayName("리뷰이가_프로젝트_멤버가_아니면_예외가_발생한다")
    void validateReviewable_revieweeNotMember_throwsException() {
        // given
        projectRepository.save(completedProject());
        projectMemberRepository.givenMember(TEST_PROJECT_GUID_1, TEST_USER_GUID_1);

        // when & then
        assertThatThrownBy(() ->
                projectMemberService.validateReviewable(
                        TEST_PROJECT_GUID_1,
                        TEST_USER_GUID_1,
                        TEST_USER_GUID_2
                )
        )
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REVIEW_NOT_A_MEMBER.getMessage());
    }
}
