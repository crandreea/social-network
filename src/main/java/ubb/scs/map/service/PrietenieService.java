package ubb.scs.map.service;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Tuplu;
import ubb.scs.map.repository.Repository;

public class PrietenieService extends AbstractService<Tuplu<Long, Long>, Prietenie> {
    public PrietenieService(Repository<Tuplu<Long, Long>, Prietenie> repository) {
        super(repository);
    }
}
