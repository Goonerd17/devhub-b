package teamdevhub.devhub.notification.core.port.in.command;

import lombok.Builder;
import teamdevhub.devhub.notification.core.domain.NotificationType;

import java.util.List;

@Builder
public record CreateNotificationCommand(
        String receiverId,
        NotificationType type,
        List<Object> messageArgs,
        String redirectTarget
) {}