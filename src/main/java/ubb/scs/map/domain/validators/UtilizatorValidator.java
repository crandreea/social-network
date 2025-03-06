package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        String errors = "";
        if (entity.getUsername().isEmpty())
            errors += "Invalid user: First name can't be null";

        if (entity.getPassword().isEmpty())
            errors += "Invalid user: Last name can't be null";

        if(!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
