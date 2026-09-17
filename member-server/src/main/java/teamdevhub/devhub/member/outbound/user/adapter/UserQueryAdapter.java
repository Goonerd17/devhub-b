package teamdevhub.devhub.member.outbound.user.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.member.core.user.port.out.UserQueryRepository;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.member.outbound.user.adapter.mapper.UserMapper;
import teamdevhub.devhub.member.outbound.user.persistence.UserQueryDao;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryRepository {

    private final UserQueryDao userQueryDao;

    @Override
    public PageResult<User> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand) {
        Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size(), Sort.by("registeredDate").descending());
        Page<UserEntity> pagedUserEntityList = userQueryDao.listUser(searchUserCommand, pageable);

        List<User> userList = pagedUserEntityList.getContent().stream()
                .map(UserMapper::toDomain)
                .toList();

        return PageResult.of(
                userList,
                pagedUserEntityList.getNumber(),
                pagedUserEntityList.getSize(),
                pagedUserEntityList.getTotalElements());
    }
}
