package teamdevhub.devhub.notification.core.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.shared.core.common.audit.AuditInfo;
import teamdevhub.devhub.notification.core.port.in.command.CreateNotificationCommand;

@Getter
@Builder
public class Notification {

    private String notificationGuid;
    private String typeCd;
    private String receiver;
    private String content;
    private boolean isChecked;
    private String link;
    private final AuditInfo auditInfo;

    public void checked() {
        this.isChecked = true;
    }

    public static Notification create(CreateNotificationCommand notificationCommand) {
        return builder()
                .typeCd(notificationCommand.type().getCategoryCode())
                .receiver(notificationCommand.receiverId())
                .content(notificationCommand.type().generateMessage(notificationCommand.messageArgs().toArray()))
                .isChecked(false)
                .link(notificationCommand.type().generateLink(notificationCommand.redirectTarget()))
                .auditInfo(AuditInfo.empty())
                .build();
    }
}
