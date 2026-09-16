package teamdevhub.devhub.web.api.home.model.response;

import teamdevhub.devhub.readmodel.core.home.port.out.LoadHomeProjectPort;

public record HomeProjectResponseDto(
        String projectGuid,
        String title,
        String category,
        String username,
        String imageFileGuid,
        String recruitmentStartDate,
        String recruitmentEndDate,
        String recruitStatus
) {

    public static HomeProjectResponseDto from(LoadHomeProjectPort.HomeProjectResult result) {
        return new HomeProjectResponseDto(
                result.projectGuid(),
                result.title(),
                result.category(),
                result.username(),
                result.imageFileGuid(),
                result.recruitmentStartDate(),
                result.recruitmentEndDate(),
                result.recruitStatus()
        );
    }
}
