package teamdevhub.devhub.member.core.user.port.in.command;

import lombok.Builder;

@Builder
public record UpdateProfileImageCommand(String userGuid, String fileGuid) {}

