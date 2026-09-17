package teamdevhub.devhub.query.core.skilltrend.domain.vo;

import lombok.Builder;

@Builder
public record PositionCount(
        String positionCd,
        long count
) {

    public static PositionCount of(String positionCd, long count) {
        return PositionCount.builder()
                .positionCd(positionCd)
                .count(count)
                .build();
    }
}
