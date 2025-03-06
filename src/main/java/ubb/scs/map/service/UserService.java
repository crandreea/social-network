package ubb.scs.map.service;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.dto.UserFilterDTO;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.database.UtilizatorDBRepository;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

public class UserService extends AbstractService<Long, Utilizator> {
    public UserService(Repository<Long, Utilizator> repository) {
        super(repository);
    }
    public Page<Utilizator> findAllOnPage(Pageable pageable) {
        return ((UtilizatorDBRepository) repository).findAllOnPage(pageable);
    }

    public Page<Utilizator> findAllOnPage(Pageable pageable, UserFilterDTO filter) {
        return ((UtilizatorDBRepository) repository).findAllOnPage(pageable, filter);
    }

}
