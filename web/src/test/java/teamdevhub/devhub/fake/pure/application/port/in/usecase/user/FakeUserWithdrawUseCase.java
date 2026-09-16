package teamdevhub.devhub.fake.pure.application.port.in.usecase.user;

import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserWithdrawUseCase;

import java.util.HashMap;
import java.util.Map;

public class FakeUserWithdrawUseCase implements UserWithdrawUseCase {

    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_EMAIL_1 = "user1@example.com";
    private static final String TEST_PASSWORD_1 = "password123!";
    private static final String TEST_USERNAME_1 = "User1";
    private static final String TEST_INTRO_1 = "Hello World";
    private static final java.util.List<String> TEST_POSITION_LIST = java.util.List.of("001");
    private static final java.util.List<String> TEST_SKILL_LIST = java.util.List.of("001");
    private static final teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget VERIFICATION_TARGET_1 =
            teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget.of(
                    teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationType.EMAIL, TEST_EMAIL_1);

    private final Map<String, User> store = new HashMap<>();

    public FakeUserWithdrawUseCase() {
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
    public void withdraw(String userGuid) {
        User user = store.get(userGuid);
        if (user != null) {
            user.withdraw();
        }
    }

    public User getUser(String userGuid) {
        return store.get(userGuid);
    }
}
