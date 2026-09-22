package teamdevhub.devhub.fake.pure.application.port.in.usecase.auth;

import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.shared.security.MemberRole;
import teamdevhub.devhub.auth.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.auth.core.auth.port.in.usecase.AuthenticatedUserUseCase;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeAuthenticatedUserUseCase implements AuthenticatedUserUseCase {

    private final Map<String, AuthenticatedUser> authenticatedUserStore = new HashMap<>();
    private final Map<String, String> passwordStore = new HashMap<>();

    public FakeAuthenticatedUserUseCase() {

        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);

        authenticatedUserStore.put(TEST_EMAIL_1, authenticatedUser);
        passwordStore.put(TEST_EMAIL_1, TEST_PASSWORD_1);
    }

    @Override
    public AuthenticatedUser getUserForReissue(String userGuid) {

        return authenticatedUserStore.values().stream()
                .filter(c -> c.userGuid().equals(userGuid))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public AuthenticatedUser authenticate(LoginCommand loginCommand) {

        AuthenticatedUser authenticatedUser = authenticatedUserStore.get(loginCommand.email());

        if (authenticatedUser == null) {
            throw new IllegalArgumentException("user not found");
        }

        String savedPassword = passwordStore.get(loginCommand.email());

        if (!savedPassword.equals(loginCommand.password())) {
            throw new IllegalArgumentException("invalid password");
        }

        return authenticatedUser;
    }
}
