package teamdevhub.devhub.member.outbound.user.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.port.out.UserRepository;
import teamdevhub.devhub.platform.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.member.outbound.user.adapter.mapper.UserMapper;
import teamdevhub.devhub.member.outbound.user.persistence.JpaUserRepository;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public void saveAdminUser(User adminUser) {
        jpaUserRepository.save(UserMapper.toEntity(adminUser));
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = jpaUserRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public User findByUserGuid(String userGuid) {
        UserEntity userEntity = jpaUserRepository.findByUserGuid(userGuid).orElseThrow(
                () -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public void updateUserProfile(User user) {
        jpaUserRepository.save(UserMapper.toEntity(user));
    }

    @Override
    public void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime) {
        jpaUserRepository.updateLastLoginDateTime(userGuid, lastLoginDateTime);
    }

    @Override
    public void delete(User user) {
        jpaUserRepository.save(UserMapper.toEntity(user));
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return jpaUserRepository.existsByUserRole(userRole);
    }

    @Override
    public Map<String, String> findNamesByUserGuid(List<String> userGuids) {
        List<Object[]> results = jpaUserRepository.findNamesByUserGuid(userGuids);

        return results.stream().collect(Collectors.toMap(
                row -> (String) row[0],
                row -> (String) row[1]
        ));
    }
}
