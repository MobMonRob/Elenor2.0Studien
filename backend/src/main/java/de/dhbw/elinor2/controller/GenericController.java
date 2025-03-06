package de.dhbw.elinor2.controller;


import de.dhbw.elinor2.services.IGenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

public abstract class GenericController<T, ID> implements IGenericController<T, ID>
{
    private final IGenericService<T, ID> service;

    protected GenericController(IGenericService<T, ID> service)
    {
        this.service = service;
    }

    @Override
    @PostMapping("")
    public ResponseEntity<T> create(@RequestBody T entity)
    {
        T savedEntity = service.create(entity);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<T> findById(@PathVariable ID id)
    {
        Optional<T> entity = service.findById(id);
        if (entity.isPresent())
        {
            return new ResponseEntity<>(entity.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    @GetMapping("")
    public ResponseEntity<Iterable<T>> findAll()
    {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T updatedEntity)
    {
        Optional<T> savedEntity = service.update(id, updatedEntity);
        if (savedEntity.isPresent())
        {
            return new ResponseEntity<>(savedEntity.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable ID id)
    {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
