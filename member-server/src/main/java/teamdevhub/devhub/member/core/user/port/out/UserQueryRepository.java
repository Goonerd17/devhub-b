package teamdevhub.devhub.member.core.user.port.out;

import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;

public interface UserQueryRepository {

    PageResult<User> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand);
}
