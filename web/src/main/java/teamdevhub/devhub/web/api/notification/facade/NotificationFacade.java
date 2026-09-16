package teamdevhub.devhub.web.api.notification.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.notification.api.NotificationInboxQuery;
import teamdevhub.devhub.web.core.notification.port.in.facade.model.NotificationResponseDto;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationFacade {

    private final NotificationInboxQuery notificationInboxQuery;

    public DataListApiResponseDto<NotificationResponseDto> getNotificationList(String userGuid) {

        List<NotificationResponseDto> notificationList = notificationInboxQuery.getNotificationList(userGuid).stream()
                .map(NotificationResponseDto::fromInboxItem)
                .toList();

        return DataListApiResponseDto.successWithDataList(
                SuccessCode.READ_SUCCESS,
                notificationList,
                null
        );
    }

    public DataApiResponseDto<Void> checkedNotification(String notificationGuid) {

        notificationInboxQuery.checkedNotification(notificationGuid);

        return DataApiResponseDto.successWithoutData(
                SuccessCode.UPDATE_SUCCESS
        );
    }
}
