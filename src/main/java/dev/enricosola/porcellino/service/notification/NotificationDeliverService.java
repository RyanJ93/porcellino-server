package dev.enricosola.porcellino.service.notification;

import dev.enricosola.porcellino.notifications.Notification;

public abstract class NotificationDeliverService<N extends Notification> {
    public abstract void deliver(N notification);
}
