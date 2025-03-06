package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Message;

public class MessageValidator implements Validator<Message> {

    @Override
    public void validate(Message message) throws ValidationException {
        if (message.getFromId().equals(message.getToId())) {
            throw new ValidationException("Sender and receiver cannot be the same.");
        }

        if (message.getMessage() == null || message.getMessage().trim().isEmpty()) {
            throw new ValidationException("Message content cannot be empty.");
        }

    }
}

