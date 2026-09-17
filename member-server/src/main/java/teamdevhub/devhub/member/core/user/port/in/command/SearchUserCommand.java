package teamdevhub.devhub.member.core.user.port.in.command;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchUserCommand(Boolean blocked, LocalDateTime joinedFrom, LocalDateTime joinedTo, String username) {}
