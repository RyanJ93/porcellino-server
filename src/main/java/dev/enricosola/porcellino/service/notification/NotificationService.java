package dev.enricosola.porcellino.service.notification;

import dev.enricosola.porcellino.factories.NotificationDeliveryServiceFactory;
import dev.enricosola.porcellino.notifications.Notification;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@Slf4j
public class NotificationService {
    private final NotificationDeliveryServiceFactory notificationDeliverServiceFactory;

    public NotificationService(NotificationDeliveryServiceFactory notificationDeliverServiceFactory) {
        this.notificationDeliverServiceFactory = notificationDeliverServiceFactory;
    }

    /**
     * Send a given notification to the recipient.
     *
     * @param notification The notification to send.
     */
    public void send(Notification notification) {
        this.notificationDeliverServiceFactory.makeForNotification(notification).deliver(notification);
    }

    /**
     * Send a list of notifications to the recipients.
     *
     * @param notificationList The list of notifications to send.
     */
    public void sendMany(List<Notification> notificationList) {
        notificationList.forEach(this::send);
    }
}
