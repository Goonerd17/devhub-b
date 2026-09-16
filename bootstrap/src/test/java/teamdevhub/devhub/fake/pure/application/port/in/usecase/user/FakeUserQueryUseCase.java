package teamdevhub.devhub.fake.pure.application.port.in.usecase.user;

import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserQueryUseCase;
import teamdevhub.devhub.platform.core.common.page.PageCommand;

import java.util.ArrayList;
import java.util.List;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserQueryUseCase implements UserQueryUseCase {

    private final List<User> userList = new ArrayList<>();

    public FakeUserQueryUseCase() {
        userList.add(
                User.builder()
                        .userGuid(TEST_USER_GUID_1)
                        .username(TEST_USERNAME_1)
                        .blocked(false)
                        .build()
        );
        userList.add(
                User.builder()
                        .userGuid(TEST_USER_GUID_2)
                        .username(TEST_USERNAME_2)
                        .blocked(false)
                        .build()
        );
    }

    @Override
    public PageResult<User> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand) {
        int page = pageCommand.page();
        int size = pageCommand.size();

        long totalElements = userList.size();

        int start = page * size;
        int end = Math.min(start + size, userList.size());

        List<User> content = start >= end ? List.of() : userList.subList(start, end);

        return PageResult.of(
                content,
                page,
                size,
                totalElements
        );
    }
}
