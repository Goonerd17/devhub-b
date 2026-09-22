package teamdevhub.devhub.notification.http.notification.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.notification.api.NotificationInboxItem;

@Getter
@SuperBuilder
@NoArgsConstructor
public class NotificationResponseDto {

    private String notificationGuid;
    private String typeCd;
    private String content;
    private String registrationDate;

    public static NotificationResponseDto fromInboxItem(NotificationInboxItem notification) {
        return builder()
                .notificationGuid(notification.notificationGuid())
                .typeCd(notification.typeCd())
                .content(notification.content())
                .registrationDate(notification.registrationDate())
                .build();
    }

}
