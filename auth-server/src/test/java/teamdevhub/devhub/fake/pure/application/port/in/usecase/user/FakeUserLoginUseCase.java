package teamdevhub.devhub.fake.pure.application.port.in.usecase.user;

import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserLoginUseCase;
import teamdevhub.devhub.member.api.MemberLoginActivityUseCase;
import teamdevhub.devhub.shared.member.AuthMemberGateway;
import teamdevhub.devhub.shared.member.AuthMemberRegistration;
import teamdevhub.devhub.shared.security.MemberRole;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserLoginUseCase implements UserLoginUseCase, MemberLoginActivityUseCase, AuthMemberGateway {

    private final Map<String, User> store = new HashMap<>();
    private final Set<String> updatedLoginUsers = new HashSet<>();

    public FakeUserLoginUseCase() {
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

        store.put(TEST_USER_GUID_1, testUser);
    }

    @Override
    public void updateLastLoginDateTime(String userGuid) {
        updatedLoginUsers.add(userGuid);
    }

    @Override
    public void validateLoginUser(String userGuid) {
        User user = store.get(userGuid);
        if (user != null) {
            user.assertActive();
        }
    }

    @Override
    public void assertMemberCanLogIn(String memberGuid) {
        validateLoginUser(memberGuid);
    }

    @Override
    public void recordSuccessfulLogin(String memberGuid) {
        updateLastLoginDateTime(memberGuid);
    }

    @Override
    public void register(AuthMemberRegistration registration) {
    }

    @Override
    public boolean adminExists() {
        return false;
    }

    @Override
    public void assertCanLogIn(String memberGuid) {
        assertMemberCanLogIn(memberGuid);
    }

    @Override
    public MemberRole findCurrentRole(String memberGuid) {
        return MemberRole.USER;
    }

    public boolean isLoginTimeUpdated(String userGuid) {
        return updatedLoginUsers.contains(userGuid);
    }

    public void givenUser(User user) {
        store.put(user.getUserGuid(), user);
    }

}

