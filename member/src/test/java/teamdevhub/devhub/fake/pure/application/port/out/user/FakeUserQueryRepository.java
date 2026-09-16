package teamdevhub.devhub.fake.pure.application.port.out.user;

import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.member.core.user.port.out.UserQueryRepository;

import java.util.*;

public class FakeUserQueryRepository implements UserQueryRepository {

    private final Map<String, User> store = new HashMap<>();

    public User save(User user) {
        store.put(user.getUserGuid(), user);
        return user;
    }

    @Override
    public PageResult<User> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand) {
        List<User> userList = new ArrayList<>(store.values());

        long totalElements = userList.size();
        int start = pageCommand.page() * pageCommand.size();
        int end = Math.min(start + pageCommand.size(), userList.size());
        List<User> pageContent = start >= end ? Collections.emptyList() : userList.subList(start, end);

        return PageResult.of(
                pageContent,
                pageCommand.page(),
                pageCommand.size(),
                totalElements
        );
    }
}
