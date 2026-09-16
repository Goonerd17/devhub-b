package teamdevhub.devhub.fake.pure.application.port.in.usecase.user;

import teamdevhub.devhub.member.core.user.port.in.command.AdminUpdateUserCommand;
import teamdevhub.devhub.member.core.user.port.in.command.BanUserCommand;
import teamdevhub.devhub.member.core.user.port.in.usecase.AdminUserManagementUseCase;

import java.util.ArrayList;
import java.util.List;

public class FakeAdminUserManagementUseCase implements AdminUserManagementUseCase {

    private final List<String> bannedUsers = new ArrayList<>();
    private final List<String> unbannedUsers = new ArrayList<>();
    private final List<AdminUpdateUserCommand> updatedUsers = new ArrayList<>();

    @Override
    public void banUser(BanUserCommand command) {
        bannedUsers.add(command.userGuid());
    }

    @Override
    public void unbanUser(String userGuid) {
        unbannedUsers.add(userGuid);
    }

    @Override
    public void updateUser(AdminUpdateUserCommand command) {
        updatedUsers.add(command);
    }

    public boolean wasBanned(String userGuid) {
        return bannedUsers.contains(userGuid);
    }

    public boolean wasUnbanned(String userGuid) {
        return unbannedUsers.contains(userGuid);
    }

    public boolean wasUpdated(String userGuid) {
        return updatedUsers.stream().anyMatch(c -> c.userGuid().equals(userGuid));
    }
}
