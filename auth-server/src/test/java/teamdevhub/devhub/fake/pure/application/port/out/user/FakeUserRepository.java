package teamdevhub.devhub.fake.pure.application.port.out.user;

import teamdevhub.devhub.shared.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.domain.vo.command.UpdateUserCommand;
import teamdevhub.devhub.member.core.user.port.out.UserRepository;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;

import java.time.LocalDateTime;
import java.util.*;

public class FakeUserRepository implements UserRepository {

    private final Map<String, User> store = new HashMap<>();
    private final List<String> calledMethods = new ArrayList<>();
    private final Map<String, LocalDateTime> lastLoginStore = new HashMap<>();
    private final Map<String, Double> mannerDegreeStore = new HashMap<>();

    @Override
    public void saveAdminUser(User adminUser) {
        store.put(adminUser.getUserGuid(), adminUser);
    }

    @Override
    public User findByUserGuid(String userGuid) {
        calledMethods.add("findByUserGuid");
        User user = store.get(userGuid);
        if (user == null) {
            throw AdapterDataException.of(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public User save(User user) {
        calledMethods.add("save");
        store.put(user.getUserGuid(), user);
        return user;
    }

    @Override
    public void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime) {
        if (store.containsKey(userGuid)) {
            calledMethods.add("updateLastLoginDateTime");
            lastLoginStore.put(userGuid, lastLoginDateTime);
        }
    }

    @Override
    public void updateUserProfile(User user) {
        User existedUser = store.get(user.getUserGuid());
        if (existedUser != null) {
            UpdateUserCommand updateUserCommand = new UpdateUserCommand(user.getUsername(), user.getIntroduction());
            existedUser.updateBasicProfile(updateUserCommand);
        }
    }

    @Override
    public void delete(User user) {
        store.put(user.getUserGuid(), user);
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return store.values().stream()
                .anyMatch(u -> u.getUserRole().equals(userRole));
    }

    @Override
    public Map<String, String> findNamesByUserGuid(List<String> userGuids) {
        Map<String, String> result = new HashMap<>();
        for (String guid : userGuids) {
            User user = store.get(guid);
            if (user != null) {
                result.put(guid, user.getUsername());
            }
        }
        return result;
    }

    public void givenUser(User user) {
        store.put(user.getUserGuid(), user);
    }

    public boolean wasCalled(String methodName) {
        return calledMethods.contains(methodName);
    }

    public long callCount(String methodName) {
        return calledMethods.stream()
                .filter(methodName::equals)
                .count();
    }

    public LocalDateTime lastLoginOf(String userGuid) {
        return lastLoginStore.get(userGuid);
    }

    public double mannerDegreeOf(String userGuid) {
        return mannerDegreeStore.getOrDefault(userGuid, 0.0);
    }
}
