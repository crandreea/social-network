package ubb.scs.map.controller;

import ubb.scs.map.domain.validators.MessageValidator;
import ubb.scs.map.domain.validators.NotificationValidator;
import ubb.scs.map.domain.validators.PrietenieValidator;
import ubb.scs.map.domain.validators.UtilizatorValidator;
import ubb.scs.map.repository.database.MessageDBRepository;
import ubb.scs.map.repository.database.NotificationDBRepository;
import ubb.scs.map.repository.database.PrietenieDBRepository;
import ubb.scs.map.repository.database.UtilizatorDBRepository;
import ubb.scs.map.service.*;

public class GlobalService {
    private static NetworkService network = null;

    public static NetworkService getNetwork() {
        if (network == null) {
            UtilizatorDBRepository utilizatorRepository = new UtilizatorDBRepository(new UtilizatorValidator());
            PrietenieDBRepository prietenieRepository = new PrietenieDBRepository(new PrietenieValidator(utilizatorRepository));
            MessageDBRepository messageRepository = new MessageDBRepository(new MessageValidator());
            NotificationDBRepository notificationRepository = new NotificationDBRepository(new NotificationValidator(utilizatorRepository));
            UserService userService = new UserService(utilizatorRepository);
            PrietenieService prietenieService = new PrietenieService(prietenieRepository);
            MessageService messageService = new MessageService(messageRepository);
            NotificationService notificationService = new NotificationService(notificationRepository);

            network = new NetworkService(userService, prietenieService, notificationService, messageService);

        }

        return network;
    }

}
