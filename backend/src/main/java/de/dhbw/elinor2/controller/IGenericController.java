package de.dhbw.elinor2.controller;

import org.springframework.http.ResponseEntity;

public interface IGenericController<Entity, Id>
{
    ResponseEntity<Entity> create(Entity entity);

    ResponseEntity<Entity> findById(Id id);

    ResponseEntity<Iterable<Entity>> findAll();

    ResponseEntity<Entity> update(Id id, Entity updatedEntity);

    ResponseEntity<Void> deleteById(Id id);
}
