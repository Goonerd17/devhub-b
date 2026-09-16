package teamdevhub.devhub.medium.outbound.user.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.member.outbound.user.adapter.UserQueryAdapter;
import teamdevhub.devhub.member.outbound.user.adapter.mapper.UserMapper;
import teamdevhub.devhub.member.outbound.user.persistence.JpaUserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@Transactional
public class UserQueryAdapterTest {

    @Autowired
    private UserQueryAdapter userQueryAdapter;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @BeforeEach
    void init() {
        jpaUserRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자_목록_조회를_하면_AdminUserSummaryResponseDto_로_된_PageResult_데이터를_반환한다")
    void getUserListAsAdminSummary() {
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

        jpaUserRepository.save(UserMapper.toEntity(testUser1));
        jpaUserRepository.save(UserMapper.toEntity(testUser2));

        SearchUserCommand searchCommand = new SearchUserCommand(null, null, null, null);

        // when
        PageResult<User> page = userQueryAdapter.listUser(searchCommand, PageCommand.of(0,10));

        // then
        assertThat(page.content()).hasSize(2);
    }
}
