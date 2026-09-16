package teamdevhub.devhub.member.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.command.UpdateUserCommand;
import teamdevhub.devhub.member.core.user.port.in.command.AdminUpdateUserCommand;
import teamdevhub.devhub.member.core.user.port.in.command.BanUserCommand;
import teamdevhub.devhub.member.core.user.port.in.usecase.AdminUserManagementUseCase;
import teamdevhub.devhub.member.core.user.port.out.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserManagementService implements AdminUserManagementUseCase {

    private final UserRepository userRepository;

    @Override
    public void banUser(BanUserCommand banUserCommand) {
        User user = userRepository.findByUserGuid(banUserCommand.userGuid());
        user.ban(banUserCommand.blockEndDate());
        userRepository.save(user);
    }

    @Override
    public void unbanUser(String userGuid) {
        User user = userRepository.findByUserGuid(userGuid);
        user.unban();
        userRepository.save(user);
    }

    @Override
    public void updateUser(AdminUpdateUserCommand adminUpdateUserCommand) {
        User user = userRepository.findByUserGuid(adminUpdateUserCommand.userGuid());
        user.updateBasicProfile(new UpdateUserCommand(adminUpdateUserCommand.username(), adminUpdateUserCommand.introduction()));
        userRepository.save(user);
    }
}
