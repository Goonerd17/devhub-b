package teamdevhub.devhub.readmodel.core.home.port.in.query;

import lombok.Builder;

@Builder
public record HomeBoardQuery(int limit, BoardSortType sortType) {

    public enum BoardSortType {
        VIEW_COUNT, LIKE_COUNT
    }

    public static HomeBoardQuery of(int limit, BoardSortType sortType) {
        return HomeBoardQuery.builder()
                .limit(limit > 0 ? limit : 6)
                .sortType(sortType != null ? sortType : BoardSortType.VIEW_COUNT)
                .build();
    }
}
