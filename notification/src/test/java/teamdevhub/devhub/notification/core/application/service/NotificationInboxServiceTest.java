package teamdevhub.devhub.notification.core.application.service;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.notification.core.domain.Notification;
import teamdevhub.devhub.notification.core.port.in.NotificationQueryUseCase;
import teamdevhub.devhub.notification.core.port.in.NotificationUseCase;
import teamdevhub.devhub.platform.core.common.audit.AuditInfo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationInboxServiceTest {
    @Test
    void projects_existing_inbox_fields_without_exposing_domain_objects() {
        NotificationQueryUseCase query = mock(NotificationQueryUseCase.class);
        NotificationUseCase command = mock(NotificationUseCase.class);
        NotificationInboxService service = new NotificationInboxService(query, command);
        Notification notification = Notification.builder()
                .notificationGuid("notification-1")
                .typeCd("type-1")
                .content("content")
                .auditInfo(AuditInfo.empty())
                .build();
        when(query.getNotificationList("member-1")).thenReturn(List.of(notification));

        var result = service.getNotificationList("member-1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).notificationGuid()).isEqualTo("notification-1");
        assertThat(result.get(0).typeCd()).isEqualTo("type-1");
        assertThat(result.get(0).content()).isEqualTo("content");
        assertThat(result.get(0).registrationDate()).isEqualTo(notification.getAuditInfo().registrantGuid());

        service.checkedNotification("notification-1");
        verify(command).checkedNotification("notification-1");
    }
}
