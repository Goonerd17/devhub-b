package teamdevhub.devhub.member.core.user.domain.vo.position;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserPositionChangeResult(boolean changed, Set<UserPosition> previousPositions, Set<UserPosition> changedPositions) {

    public static UserPositionChangeResult changed(Set<UserPosition> previousPositions, Set<UserPosition> changedPositions) {
        return new UserPositionChangeResult(true, Set.copyOf(previousPositions), Set.copyOf(changedPositions));
    }

    public static UserPositionChangeResult unchanged(Set<UserPosition> unchangedPositions) {
        return new UserPositionChangeResult(false, Set.copyOf(unchangedPositions), Set.copyOf(unchangedPositions));
    }
}
