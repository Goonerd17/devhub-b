package teamdevhub.devhub.admin.http.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.admin.http.user.model.AdminUserDetailResponseDto;
import teamdevhub.devhub.admin.http.user.model.UserBasicResponseDto;
import teamdevhub.devhub.shared.member.*;
import teamdevhub.devhub.shared.security.AdminPasswordReset;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserHttpFacade {
    private final AdminMemberGateway memberGateway;
    private final AdminPasswordReset adminPasswordReset;

    public PageResult<UserBasicResponseDto> listUsers(AdminMemberSearch command, PageCommand page) {
        AdminMemberPage result = memberGateway.search(command, page.page(), page.size());
        return PageResult.of(result.content().stream().map(UserBasicResponseDto::fromView).toList(), result.page(), result.size(), result.totalElements());
    }
    public AdminUserDetailResponseDto getUserDetail(String guid) { return AdminUserDetailResponseDto.fromView(memberGateway.detail(guid)); }
    public void updateUser(AdminMemberUpdate command) { memberGateway.update(command); }
    public void banUser(AdminMemberBan command) { memberGateway.ban(command); }
    public void unbanUser(String guid) { memberGateway.unban(guid); }
    public void resetPassword(String guid, String password) { adminPasswordReset.reset(guid, password); }
}
