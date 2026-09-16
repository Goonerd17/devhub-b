package teamdevhub.devhub.member.core.user.port.in.usecase;

import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.platform.core.common.page.PageCommand;

public interface UserQueryUseCase {

    PageResult<User> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand);
}
