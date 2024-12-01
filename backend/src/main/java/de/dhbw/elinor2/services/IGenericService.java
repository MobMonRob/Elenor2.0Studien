package de.dhbw.elinor2.services;

import java.util.Optional;

public interface IGenericService<Entity, Id>
{
    Entity create(Entity entity);

    Optional<Entity> findById(Id id);

    Iterable<Entity> findAll();

    Optional<Entity> update(Id id, Entity updatedEntity);

    void deleteById(Id id);

    boolean existsById(Id id);
}
