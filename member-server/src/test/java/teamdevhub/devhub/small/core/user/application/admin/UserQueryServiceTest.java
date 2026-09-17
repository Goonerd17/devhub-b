package teamdevhub.devhub.member.core.user.application.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.member.core.user.application.admin.UserQueryService;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserQueryServiceTest {

    private UserQueryService userQueryService;

    private FakeUserQueryRepository userQueryRepository;

    @BeforeEach
    void init() {
        userQueryRepository = new FakeUserQueryRepository();

        userQueryService = new UserQueryService(userQueryRepository);
    }

    @Test
    @DisplayName("사용자_목록을_조회할_수_있다")
    void canFetchUserList() {
        // given
        SignupUserCommand signupUserCommand1 = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand1 = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand1.username(), signupUserCommand1.introduction(), signupUserCommand1.positionList(), signupUserCommand1.skillList());
        User testUser1 = User.createGeneralUser(generalCreateUserCommand1);

        SignupUserCommand signupUserCommand2 = SignupUserCommand.builder()
                .email(TEST_EMAIL_2)
                .password(TEST_PASSWORD_2)
                .username(TEST_USERNAME_2)
                .introduction(TEST_INTRO_2)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_2)
                .build();
        CreateUserCommand generalCreateUserCommand2 = new CreateUserCommand(TEST_USER_GUID_2, signupUserCommand2.username(), signupUserCommand2.introduction(), signupUserCommand2.positionList(), signupUserCommand2.skillList());
        User testUser2 = User.createGeneralUser(generalCreateUserCommand2);

        userQueryRepository.save(testUser1);
        userQueryRepository.save(testUser2);

        SearchUserCommand searchUserCommand = SearchUserCommand.builder()
                .blocked(null)
                .username(null)
                .joinedTo(null)
                .joinedFrom(null)
                .build();

        PageCommand pageCommand = PageCommand.of(0,10);

        // when
        PageResult<User> pagedUserList = userQueryService.listUser(searchUserCommand, pageCommand);
        User user = pagedUserList.content().get(0);

        // then
        assertThat(pagedUserList).isNotNull();
        assertThat(pagedUserList.content().size()).isEqualTo(2);
        assertThat(pagedUserList.totalPages()).isEqualTo(1);
        assertThat(user.getUserGuid()).isEqualTo(testUser1.getUserGuid());
    }
}