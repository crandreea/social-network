package ubb.scs.map.repository.paging;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

import java.sql.SQLException;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findAllOnPage(Pageable pageable) throws SQLException;
}
