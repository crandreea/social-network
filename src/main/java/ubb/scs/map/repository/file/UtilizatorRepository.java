package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;
import java.util.Arrays;
import java.util.List;

public class UtilizatorRepository extends AbstractFileRepository<Long, Utilizator>{

    public UtilizatorRepository(Validator<Utilizator> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Utilizator createEntity(String line) {
        List<String> fields = Arrays.asList(line.split(";"));
        Long id = Long.parseLong(fields.get(0));
        String firstName = fields.get(1);
        String lastName = fields.get(2);
        Utilizator u = new Utilizator(firstName, lastName);

        u.setId(id);
        return u;
    }

    @Override
    public String saveEntity(Utilizator entity) {
        return entity.getId() + ";" + entity.getUsername() + ";" + entity.getPassword();
    }
}
