package teamdevhub.devhub.member.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserWithdrawUseCase;
import teamdevhub.devhub.member.core.user.port.out.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWithdrawService implements UserWithdrawUseCase {

    private final UserRepository userRepository;

    @Override
    public void withdraw(String userGuid) {
        User user = userRepository.findByUserGuid(userGuid);
        user.withdraw();
        userRepository.delete(user);
    }
}
