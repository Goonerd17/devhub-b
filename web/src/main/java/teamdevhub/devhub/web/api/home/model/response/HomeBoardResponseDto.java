package teamdevhub.devhub.web.api.home.model.response;

import teamdevhub.devhub.readmodel.core.home.port.out.LoadHomeBoardPort;

public record HomeBoardResponseDto(
        String boardGuid,
        String title,
        String categoryCd,
        String username,
        int viewCount,
        long likeCount,
        String registeredDate
) {

    public static HomeBoardResponseDto from(LoadHomeBoardPort.HomeBoardResult result) {
        return new HomeBoardResponseDto(
                result.boardGuid(),
                result.title(),
                result.categoryCd(),
                result.username(),
                result.viewCount(),
                result.likeCount(),
                result.registeredDate()
        );
    }
}
