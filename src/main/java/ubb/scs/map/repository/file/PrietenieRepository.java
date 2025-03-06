package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Tuplu;
import ubb.scs.map.domain.validators.Validator;

import java.time.LocalDate;

public class PrietenieRepository extends AbstractFileRepository<Tuplu<Long, Long>, Prietenie> {
    public PrietenieRepository(Validator<Prietenie> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Prietenie createEntity(String line) {
        String[] splited = line.split(";");
        Long idUser1 = Long.parseLong(splited[0]);
        Long idUser2 = Long.parseLong(splited[1]);
        LocalDate date = LocalDate.parse(splited[2]);

        Prietenie pr = new Prietenie(idUser1, idUser2);
        pr.setDate(date);
        pr.setId(new Tuplu<>(idUser1, idUser2));
        return pr;
    }

    @Override
    public String saveEntity(Prietenie entity) {
        return entity.getIdUser1() + ";" + entity.getIdUser2() + ";" + entity.getDate();

    }
}
