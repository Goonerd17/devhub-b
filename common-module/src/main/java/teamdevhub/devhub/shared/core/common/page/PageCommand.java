package teamdevhub.devhub.shared.core.common.page;

import lombok.Builder;

@Builder
public record PageCommand(int page, int size) {

    private static final int MAX_SIZE = 100;

    public static PageCommand of(int page, int size) {
        int safeSize = Math.max(1, Math.min(size, MAX_SIZE));
        int safePage = Math.max(page, 0);
        return new PageCommand(safePage, safeSize);
    }
}
