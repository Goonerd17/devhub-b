package teamdevhub.devhub.query.http.home.model.response;

import teamdevhub.devhub.query.core.home.port.out.LoadHomeBoardPort;

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

