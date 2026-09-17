package teamdevhub.devhub.member.core.user.application.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserQueryUseCase;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.member.core.user.port.out.UserQueryRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserQueryService implements UserQueryUseCase {

    private final UserQueryRepository userQueryRepository;

    @Override
    public PageResult<User> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand) {
        return userQueryRepository.listUser(searchUserCommand, pageCommand);
    }
}
