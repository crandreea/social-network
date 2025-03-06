package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Notification;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

import java.sql.SQLException;

public class NotificationValidator implements Validator<Notification> {

    private final Repository<Long, Utilizator> userRepository;

    public NotificationValidator(Repository<Long, Utilizator> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(Notification notification) {
        if (notification == null) {
            throw new ValidationException("Notification must not be null");
        }

        if (notification.getDescription().isEmpty()) {
            throw new ValidationException("Description must not be empty");
        }

        if (notification.getDescription().length() >= 300) {
            throw new ValidationException("Description must be less than 300 characters");
        }
    }
}

