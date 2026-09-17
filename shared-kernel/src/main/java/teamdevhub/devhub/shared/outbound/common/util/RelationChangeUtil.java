package teamdevhub.devhub.shared.outbound.common.util;

import java.util.HashSet;
import java.util.Set;

public final class RelationChangeUtil {

    private RelationChangeUtil() {}

    public static <T> RelationChange<T> change(Set<T> existing, Set<T> incoming) {
        Set<T> toDelete = new HashSet<>(existing);
        toDelete.removeAll(incoming);

        Set<T> toInsert = new HashSet<>(incoming);
        toInsert.removeAll(existing);

        return RelationChange.of(toInsert, toDelete);
    }

    public record RelationChange<T>(Set<T> toInsert, Set<T> toDelete) {

        public boolean isEmpty() {
            return toInsert.isEmpty() && toDelete.isEmpty();
        }

        public static <T> RelationChange<T> of(Set<T> toInsert, Set<T> toDelete) {
            return new RelationChange<>(Set.copyOf(toInsert), Set.copyOf(toDelete));
        }
    }
}
