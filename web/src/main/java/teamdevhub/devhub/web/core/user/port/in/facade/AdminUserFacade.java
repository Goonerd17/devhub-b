package teamdevhub.devhub.web.core.user.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import teamdevhub.devhub.identity.api.credential.AdminPasswordReset;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.web.core.project.port.in.facade.model.UserProjectResponseDto;
import teamdevhub.devhub.community.api.ReportPage;
import teamdevhub.devhub.community.api.ReportQuery;
import teamdevhub.devhub.project.api.AdminMemberProjectQuery;
import teamdevhub.devhub.community.api.ReportProcessing;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.in.command.AdminUpdateUserCommand;
import teamdevhub.devhub.member.core.user.port.in.command.BanUserCommand;
import teamdevhub.devhub.member.core.user.port.in.usecase.AdminUserManagementUseCase;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserProfileUseCase;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserQueryUseCase;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.web.core.user.port.in.facade.model.UserBasicResponseDto;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserFacade {

    private final UserQueryUseCase userQueryUseCase;
    private final UserProfileUseCase userProfileUseCase;
    private final AdminUserManagementUseCase adminUserManagementUseCase;
    private final AdminPasswordReset adminPasswordReset;
    private final ReportQuery reportQuery;
    private final ReportProcessing reportProcessing;
    private final AdminMemberProjectQuery adminMemberProjectQuery;

    public PageResult<UserBasicResponseDto> listUsers(SearchUserCommand searchUserCommand, PageCommand pageCommand) {
        PageResult<User> result = userQueryUseCase.listUser(searchUserCommand, pageCommand);
        List<UserBasicResponseDto> userBasicResponseDtoList = result.content().stream()
                .map(UserBasicResponseDto::fromDomain)
                .toList();
        return PageResult.of(userBasicResponseDtoList, result.page(), result.size(), result.totalElements());
    }

    public User getUserDetail(String userGuid) {
        return userProfileUseCase.getCurrentUserProfile(userGuid);
    }

    public void updateUser(AdminUpdateUserCommand adminUpdateUserCommand) {
        adminUserManagementUseCase.updateUser(adminUpdateUserCommand);
    }

    public void resetUserPassword(String userGuid, String newPassword) {
        adminPasswordReset.reset(userGuid, newPassword);
    }

    public void banUser(BanUserCommand banUserCommand) {
        adminUserManagementUseCase.banUser(banUserCommand);
    }

    public void unbanUser(String userGuid) {
        adminUserManagementUseCase.unbanUser(userGuid);
    }

    /**
     * approval state 의미 및 필요성
     * @param userGuid
     * @param pageCommand
     * @return
     */
    public PageResult<UserProjectResponseDto> getUserProjects(String userGuid, PageCommand pageCommand) {
		var projection = adminMemberProjectQuery.findRegisteredProjects(userGuid, pageCommand.page(), pageCommand.size());
		List<UserProjectResponseDto> projected = projection.content().stream().map(UserProjectResponseDto::fromProjection).toList();
		return PageResult.of(projected, projection.page(), projection.size(), projection.totalElements());
    }

    public PageResult<UserProjectResponseDto> getUserApplyProjects(String userGuid, PageCommand pageCommand) {
		var projection = adminMemberProjectQuery.findAppliedProjects(userGuid, pageCommand.page(), pageCommand.size());
		List<UserProjectResponseDto> projected = projection.content().stream().map(UserProjectResponseDto::fromProjection).toList();
		return PageResult.of(projected, projection.page(), projection.size(), projection.totalElements());
    }

    public ReportPage getUserReceivedReports(String userGuid, PageCommand pageCommand) {
        return reportQuery.findReceived(userGuid, pageCommand.page(), pageCommand.size());
    }

    public ReportPage getUserSubmittedReports(String userGuid, PageCommand pageCommand) {
        return reportQuery.findSubmitted(userGuid, pageCommand.page(), pageCommand.size());
    }

    public ReportPage getAllReports(PageCommand pageCommand) {
        return reportQuery.findAll(pageCommand.page(), pageCommand.size());
    }

    public void processReport(String reportGuid) {
        reportProcessing.process(reportGuid);
    }
}
