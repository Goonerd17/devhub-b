package teamdevhub.devhub.member.outbound.user.adapter.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.member.outbound.user.adapter.mapper.UserMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserMapperTest {

    @Test
    @DisplayName("User_瑜?UserEntity_濡?蹂?섑븷_???덈떎")
    void convertDomainToEntity() {
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
        UserEntity userEntity = UserMapper.toEntity(testUser);

        // then
        assertThat(userEntity.getUserGuid()).isEqualTo(testUser.getUserGuid());
        assertThat(userEntity.getUserRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("UserEntity_瑜?User_濡?蹂?섑븷_???덈떎")
    void convertEntityToDomain() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userGuid(TEST_USER_GUID_1)
                .username(TEST_USERNAME_1)
                .userRole(UserRole.USER)
                .introduction(TEST_INTRO_1)
                .build();

        // when
        User user = UserMapper.toDomain(userEntity);

        // then
        assertThat(user.getUserGuid()).isEqualTo(userEntity.getUserGuid());
    }
}



