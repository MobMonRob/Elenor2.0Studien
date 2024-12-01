package de.dhbw.elinor2.controller;


import de.dhbw.elinor2.services.IGenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

public abstract class GenericController<Entity, Id> implements IGenericController<Entity, Id>
{
    private final IGenericService<Entity, Id> service;

    public GenericController(IGenericService<Entity, Id> service)
    {
        this.service = service;
    }

    @Override
    @PostMapping("")
    public ResponseEntity<Entity> create(@RequestBody Entity entity)
    {
        Entity savedEntity = service.create(entity);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Entity> findById(@PathVariable Id id)
    {
        Optional<Entity> entity = service.findById(id);
        if (entity.isPresent())
        {
            return new ResponseEntity<>(entity.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    @GetMapping("")
    public ResponseEntity<Iterable<Entity>> findAll()
    {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Entity> update(@PathVariable Id id, @RequestBody Entity updatedEntity)
    {
        Optional<Entity> savedEntity = service.update(id, updatedEntity);
        if (savedEntity.isPresent())
        {
            return new ResponseEntity<>(savedEntity.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Id id)
    {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
