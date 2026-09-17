package teamdevhub.devhub.member.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserLoginUseCase;
import teamdevhub.devhub.shared.time.TimeProvider;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.out.UserRepository;
import teamdevhub.devhub.member.api.MemberLoginActivityUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class UserLoginService implements UserLoginUseCase, MemberLoginActivityUseCase {

    private final TimeProvider timeProvider;
    private final UserRepository userRepository;

    @Override
    public void validateLoginUser(String userGuid) {
        User user = userRepository.findByUserGuid(userGuid);
        user.assertActive();
    }

    @Override
    public void updateLastLoginDateTime(String userGuid) {
        userRepository.updateLastLoginDateTime(userGuid, timeProvider.now());
    }

    @Override
    public void assertMemberCanLogIn(String memberGuid) {
        validateLoginUser(memberGuid);
    }

    @Override
    public void recordSuccessfulLogin(String memberGuid) {
        updateLastLoginDateTime(memberGuid);
    }
}
