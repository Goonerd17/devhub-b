package teamdevhub.devhub.medium.outbound.user.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.member.core.user.domain.vo.command.UpdateUserCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.member.outbound.user.adapter.UserAdapter;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.member.outbound.user.adapter.mapper.UserMapper;
import teamdevhub.devhub.member.outbound.user.persistence.JpaUserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@Transactional
class UserAdapterTest {

    @Autowired
    private UserAdapter userAdapter;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @BeforeEach
    void init() {
        jpaUserRepository.deleteAll();
    }

    @Test
    @DisplayName("愿由ъ옄_怨꾩젙????ν븳??")
    void saveAdminAccount() {
        // given
        SignupAdminCommand signupAdminCommand = SignupAdminCommand.builder()
                .userGuid(null)
                .email(ADMIN_EMAIL_1)
                .password(ADMIN_PASSWORD_1)
                .username(ADMIN_USERNAME_1)
                .introduction("")
                .positionList(List.of())
                .skillList(List.of())
                .verificationTarget(null)
                .build();
        CreateUserCommand adminCreateUserCommand = new CreateUserCommand(ADMIN_USER_GUID_1, signupAdminCommand.username(), signupAdminCommand.introduction(), signupAdminCommand.positionList(), signupAdminCommand.skillList());
        User adminUser = User.createAdminUser(adminCreateUserCommand);

        // when
        userAdapter.saveAdminUser(adminUser);

        // then
        UserEntity saved = jpaUserRepository.findByUserGuid(adminUser.getUserGuid()).orElse(null);
        assertThat(saved).isNotNull();
    }

    @Test
    @DisplayName("?덈줈???ъ슜?먮?_?앹꽦?섎㈃_?ъ슜???뺣낫瑜???ν븳??")
    void saveUser() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        // when
        User savedUser = userAdapter.save(testUser);

        // then
        assertThat(savedUser.getUserGuid()).isEqualTo(testUser.getUserGuid());
        assertThat(savedUser.getUsername()).isEqualTo(testUser.getUsername());
    }

    @Test
    @DisplayName("?ъ슜???앸퀎?ㅻ줈_User_瑜?議고쉶?쒕떎")
    void getUserByIdentifier() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        jpaUserRepository.save(UserMapper.toEntity(testUser));

        // when
        User foundUser = userAdapter.findByUserGuid(testUser.getUserGuid());

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserGuid()).isEqualTo(testUser.getUserGuid());
    }

    @Test
    @DisplayName("?ъ슜???꾨줈???뺣낫瑜??섏젙?섎㈃_蹂寃쎈맂_媛믪씠_??λ맂??")
    void updateUserProfile() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        jpaUserRepository.save(UserMapper.toEntity(testUser));
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(NEW_USERNAME, NEW_INTRO);
        testUser.updateBasicProfile(updateUserCommand);

        // when
        userAdapter.updateUserProfile(testUser);

        // then
        User updatedUser = jpaUserRepository.findByUserGuid(testUser.getUserGuid())
                .map(UserMapper::toDomain)
                .orElseThrow();

        assertThat(updatedUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(updatedUser.getIntroduction()).isEqualTo(testUser.getIntroduction());
    }

    @Test
    @DisplayName("?뚯썝?덊눜???ъ슜?먯쓽_deleted_媛믪?_true_?대떎")
    void isDeletedUser() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);


        testUser.withdraw();

        // when
        userAdapter.delete(testUser);

        // then
        assertThat(jpaUserRepository.findByUserGuid(testUser.getUserGuid())
                .orElseThrow()
                .isDeleted())
                .isTrue();
    }

    @Test
    @DisplayName("?ъ슜??沅뚰븳???쇱튂?쒕떎硫?true_瑜?諛섑솚?쒕떎")
    void isUserRoleMatched() {
        // given
        SignupAdminCommand signupAdminCommand = SignupAdminCommand.builder()
                .userGuid(null)
                .email(ADMIN_EMAIL_1)
                .password(ADMIN_PASSWORD_1)
                .username(ADMIN_USERNAME_1)
                .introduction("")
                .positionList(List.of())
                .skillList(List.of())
                .verificationTarget(null)
                .build();
        CreateUserCommand adminCreateUserCommand = new CreateUserCommand(ADMIN_USER_GUID_1, signupAdminCommand.username(), signupAdminCommand.introduction(), signupAdminCommand.positionList(), signupAdminCommand.skillList());
        User adminUser = User.createAdminUser(adminCreateUserCommand);

        jpaUserRepository.save(UserMapper.toEntity(adminUser));

        // when
        boolean exists = userAdapter.existsByUserRole(UserRole.ADMIN);

        // then
        assertThat(exists).isTrue();
    }
}
