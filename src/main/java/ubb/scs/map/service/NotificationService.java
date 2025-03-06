package ubb.scs.map.service;

import ubb.scs.map.domain.Notification;
import ubb.scs.map.repository.Repository;

public class NotificationService extends AbstractService<Long, Notification> {

    public NotificationService(Repository<Long, Notification> repository) {
        super(repository);
    }
}
