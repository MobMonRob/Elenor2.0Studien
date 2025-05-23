package de.dhbw.elinor2.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public abstract class GenericService<T, ID> implements IGenericService<T, ID>
{

    private final JpaRepository<T, ID> repository;

    protected GenericService(JpaRepository<T, ID> repository)
    {
        this.repository = repository;
    }

    @Override
    @Transactional
    public T create(T t)
    {
        return repository.save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(ID id)
    {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<T> findAll()
    {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Optional<T> update(ID id, T updatedEntity)
    {
        Optional<T> existing = repository.findById(id);

        if (existing.isEmpty())
        {
            return Optional.empty();
        }

        T editedEntity = updateEntity(existing.get(), updatedEntity);
        return Optional.of(repository.save(editedEntity));
    }

    @Override
    @Transactional
    public void deleteById(ID id)
    {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ID id)
    {
        return repository.existsById(id);
    }

    protected abstract T updateEntity(T entity, T updatedEntity);
}
