package teamdevhub.devhub.fake.pure.application.port.out.user;

import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.member.core.user.port.out.UserPositionRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FakeUserPositionRepository implements UserPositionRepository {

    private final Map<String, Set<UserPosition>> store = new HashMap<>();
    public boolean replaceCalled = false;

    @Override
    public Set<UserPosition> findByUserGuid(String userGuid) {
        return store.getOrDefault(userGuid, Set.of());
    }

    @Override
    public void replace(Set<UserPosition> previousPositions, Set<UserPosition> currentPositions) {
        replaceCalled = true;
        if (currentPositions == null) currentPositions = Set.of();
        if (previousPositions == null) previousPositions = Set.of();

        String userGuid = !currentPositions.isEmpty()
                ? currentPositions.iterator().next().userGuid()
                : previousPositions.iterator().next().userGuid();

        Set<UserPosition> existing = store.getOrDefault(userGuid, new HashSet<>());

        Set<UserPosition> toDelete = new HashSet<>(existing);
        toDelete.removeAll(currentPositions);

        Set<UserPosition> toInsert = new HashSet<>(currentPositions);
        toInsert.removeAll(existing);

        existing.removeAll(toDelete);
        existing.addAll(toInsert);

        store.put(userGuid, existing);
    }

    @Override
    public void saveAll(Set<UserPosition> positions) {
        if (positions == null || positions.isEmpty()) {
            return;
        }

        String userGuid = positions.iterator().next().userGuid();

        store.put(userGuid, new HashSet<>(positions));
    }
}
