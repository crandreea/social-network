package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.database.UtilizatorDBRepository;
import ubb.scs.map.repository.file.UtilizatorRepository;
import ubb.scs.map.repository.memory.InMemoryRepository;

import java.util.Optional;

public class PrietenieValidator implements Validator<Prietenie>{

    private final UtilizatorDBRepository repo;

    public PrietenieValidator(UtilizatorDBRepository repo) {

        this.repo = repo;
    }

    @Override
    public void validate(Prietenie entity) throws ValidationException {
        Optional<Utilizator> u1 = repo.findOne(entity.getIdUser1());
        Optional<Utilizator> u2 = repo.findOne(entity.getIdUser2());

        if (entity.getIdUser1() == null || entity.getIdUser2() == null)
            throw new ValidationException("Id cannot be null");
        if (u1.isEmpty() || u2.isEmpty())
            throw new ValidationException("Id doesn't exist");
    }
}
