package ubb.scs.map.service;

import ubb.scs.map.domain.Entity;

import java.sql.SQLException;
import java.util.Optional;

public interface Service<ID, E extends Entity<ID>> {

    Optional<E> findOne(ID id) throws SQLException;

    Iterable<E> findAll();

    void save(E entity);

    Optional<E> delete(ID id);

    void update(E entity);
}