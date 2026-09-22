package teamdevhub.devhub.member.http.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.in.command.*;
import teamdevhub.devhub.member.core.user.port.in.usecase.*;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.member.*;

@RestController
@RequestMapping("/internal/admin/members")
@RequiredArgsConstructor
public class AdminMemberInternalController {
    private final UserQueryUseCase userQueryUseCase;
    private final UserProfileUseCase userProfileUseCase;
    private final AdminUserManagementUseCase managementUseCase;

    @PostMapping("/search")
    public AdminMemberPage search(@RequestBody AdminMemberSearch search,
            @RequestParam int page, @RequestParam int size) {
        var command = SearchUserCommand.builder().blocked(search.blocked()).joinedFrom(search.joinedFrom())
                .joinedTo(search.joinedTo()).username(search.username()).build();
        var result = userQueryUseCase.listUser(command, PageCommand.of(page, size));
        return new AdminMemberPage(result.content().stream().map(this::view).toList(),
                result.page(), result.size(), result.totalElements());
    }

    @GetMapping("/{userGuid}")
    public AdminMemberView detail(@PathVariable String userGuid) {
        return view(userProfileUseCase.getCurrentUserProfile(userGuid));
    }

    @PutMapping("/{userGuid}")
    public void update(@PathVariable String userGuid, @RequestBody AdminMemberUpdate update) {
        managementUseCase.updateUser(AdminUpdateUserCommand.builder().userGuid(userGuid)
                .username(update.username()).introduction(update.introduction()).build());
    }

    @PostMapping("/{userGuid}/ban")
    public void ban(@PathVariable String userGuid, @RequestBody AdminMemberBan ban) {
        managementUseCase.banUser(BanUserCommand.builder().userGuid(userGuid).blockEndDate(ban.blockEndDate()).build());
    }

    @PostMapping("/{userGuid}/unban")
    public void unban(@PathVariable String userGuid) { managementUseCase.unbanUser(userGuid); }

    private AdminMemberView view(User user) {
        return new AdminMemberView(user.getUserGuid(), user.getUsername(), user.getUserRole().name(),
                user.getIntroduction(), user.getFileGuid(), user.getMannerDegree(), user.isBlocked(),
                user.getBlockEndDate(), user.isDeleted(), user.getLastLoginDateTime(),
                user.getPositions().stream().map(position -> position.positionCd()).toList(),
                user.getSkills().stream().map(skill -> skill.skillCd()).toList(),
                user.getAuditInfo().registrantGuid(), user.getAuditInfo().registeredDate(),
                user.getAuditInfo().modifierGuid(), user.getAuditInfo().modifiedDate());
    }
}
