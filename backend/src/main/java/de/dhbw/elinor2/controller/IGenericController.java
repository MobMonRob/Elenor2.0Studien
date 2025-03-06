package de.dhbw.elinor2.controller;

import org.springframework.http.ResponseEntity;

public interface IGenericController<T, ID>
{
    ResponseEntity<T> create(T entity);

    ResponseEntity<T> findById(ID id);

    ResponseEntity<Iterable<T>> findAll();

    ResponseEntity<T> update(ID id, T updatedEntity);

    ResponseEntity<Void> deleteById(ID id);
}
