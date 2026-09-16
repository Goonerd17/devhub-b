package teamdevhub.devhub.web.api.skilltrend.model.response;

import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.PositionCount;

public record PositionCountResponseDto(
        String positionCd,
        long count
) {

    public static PositionCountResponseDto from(PositionCount positionCount) {
        return new PositionCountResponseDto(positionCount.positionCd(), positionCount.count());
    }
}
