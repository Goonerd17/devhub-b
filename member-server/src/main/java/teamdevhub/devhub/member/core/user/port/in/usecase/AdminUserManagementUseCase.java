package teamdevhub.devhub.member.core.user.port.in.usecase;

import teamdevhub.devhub.member.core.user.port.in.command.AdminUpdateUserCommand;
import teamdevhub.devhub.member.core.user.port.in.command.BanUserCommand;

public interface AdminUserManagementUseCase {

    void banUser(BanUserCommand command);
    void unbanUser(String userGuid);
    void updateUser(AdminUpdateUserCommand command);
}
