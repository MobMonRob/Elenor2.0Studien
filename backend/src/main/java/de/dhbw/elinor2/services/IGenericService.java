package de.dhbw.elinor2.services;

import java.util.Optional;

public interface IGenericService<T, ID>
{
    T create(T entity);

    Optional<T> findById(ID id);

    Iterable<T> findAll();

    Optional<T> update(ID id, T updatedEntity);

    void deleteById(ID id);

    boolean existsById(ID id);
}
