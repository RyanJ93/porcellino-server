package dev.enricosola.porcellino.factories;

import dev.enricosola.porcellino.exception.UnsupportedNotificationTypeException;
import dev.enricosola.porcellino.service.notification.EmailNotificationDeliverService;
import dev.enricosola.porcellino.service.notification.NotificationDeliverService;
import dev.enricosola.porcellino.notifications.EmailNotification;
import dev.enricosola.porcellino.notifications.Notification;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class NotificationDeliveryServiceFactory {
    private final ApplicationContext applicationContext;

    public NotificationDeliveryServiceFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Return the delivery service to use according to given notification instance.
     *
     * @param notification The notification instance to be sent.
     *
     * @return The corresponding delivery service.
     *
     * @throws UnsupportedNotificationTypeException If given notification is not supported.
     */
    public NotificationDeliverService makeForNotification(Notification notification) {
        if ( notification instanceof EmailNotification ){
            return this.applicationContext.getBean(EmailNotificationDeliverService.class);
        }
        throw new UnsupportedNotificationTypeException("Unsupported notification type: " + notification.getClass().getSimpleName());
    }
}
