package teamdevhub.devhub.shared.core.common.page;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResult<T>(List<T> content, int page, int size, long totalElements, int totalPages, boolean first, boolean last) {

    public static <T> PageResult<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = calculateTotalPages(totalElements, size);
        boolean first = isFirst(page);
        boolean last = isLast(page, totalPages);
        return new PageResult<>(content, page, size, totalElements, totalPages, first, last);
    }

    private static int calculateTotalPages(long totalElements, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        }

        return (int) Math.ceil((double) totalElements / size);
    }

    private static boolean isFirst(int page) {
        return page <= 0;
    }

    private static boolean isLast(int page, int totalPages) {
        return page >= totalPages - 1;
    }
}
