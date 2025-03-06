package ubb.scs.map.service;

import ubb.scs.map.domain.Message;
import ubb.scs.map.repository.Repository;

public class MessageService extends AbstractService<Long, Message> {

    public MessageService(Repository<Long, Message> repository) {
        super(repository);
    }
}
