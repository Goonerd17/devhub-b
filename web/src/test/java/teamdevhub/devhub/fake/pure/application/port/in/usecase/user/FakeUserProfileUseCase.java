package teamdevhub.devhub.fake.pure.application.port.in.usecase.user;

import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.member.core.user.domain.vo.command.UpdateUserCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileCommand;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileImageCommand;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserProfileUseCase;

import java.util.HashMap;
import java.util.Map;

public class FakeUserProfileUseCase implements UserProfileUseCase {

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
    public boolean called = false;
    public String lastUserGuid;
    public double lastScore;

    public FakeUserProfileUseCase() {
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
    public User getUserInfo(String userGuid) {
        return store.get(userGuid);
    }

    @Override
    public User getCurrentUserProfile(String userGuid) {
        return store.get(userGuid);
    }

    @Override
    public void updateProfileImage(UpdateProfileImageCommand updateProfileImageCommand) {

    }

    @Override
    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        User user = store.get(updateProfileCommand.userGuid());
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(updateProfileCommand.username(), updateProfileCommand.introduction());
        user.updateBasicProfile(updateUserCommand);
        user.changePositions(updateProfileCommand.positions());
        user.changeSkills(updateProfileCommand.skills());
    }

    @Override
    public void updateUserMannerDegree(String userGuid, double score) {
        this.called = true;
        this.lastUserGuid = userGuid;
        this.lastScore = score;
    }
}
