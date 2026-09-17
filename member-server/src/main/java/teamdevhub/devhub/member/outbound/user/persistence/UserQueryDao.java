package teamdevhub.devhub.member.outbound.user.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserEntity;

public interface UserQueryDao {

    Page<UserEntity> listUser(SearchUserCommand searchUserCommand, Pageable pageable);
}
