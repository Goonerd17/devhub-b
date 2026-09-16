package teamdevhub.devhub.web.core.user.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserWithdrawUseCase;
import teamdevhub.devhub.web.core.user.port.in.facade.UserWithdrawFacade;

import static org.assertj.core.api.Assertions.assertThat;

public class UserWithdrawFacadeTest {

    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";

    private UserWithdrawFacade userWithdrawFacade;

    private FakeUserWithdrawUseCase userWithdrawUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;

    @BeforeEach
    void init() {
        userWithdrawUseCase = new FakeUserWithdrawUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();

        userWithdrawFacade = new UserWithdrawFacade(
                userWithdrawUseCase,
                authenticationUseCase
        );
    }

    @Test
    @DisplayName("withdrawUser_는_유저를_삭제_처리한다")
    void withdrawUserDeletesUser() {
        // given, when
        userWithdrawFacade.withdraw(TEST_USER_GUID_1);

        // then
        User deletedUser = userWithdrawUseCase.getUser(TEST_USER_GUID_1);
        assertThat(deletedUser.isDeleted()).isTrue();
    }
}
