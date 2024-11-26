package de.dhbw.elinor2.controller;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.services.ExternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/externs")
public class ExternController
{

    @Autowired
    private ExternService externService;

    @PostMapping("")
    public ResponseEntity<Extern> createExtern(@RequestBody Extern extern)
    {
        Extern savedExtern = externService.createExtern(extern);
        return new ResponseEntity<>(savedExtern, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Extern> getExternById(@PathVariable UUID id)
    {
        Optional<Extern> extern = externService.getExternById(id);
        if (extern.isPresent())
        {
            return new ResponseEntity<>(extern.get(), HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Extern not found");
    }

    @GetMapping("")
    public List<Extern> getAllExtern()
    {
        return externService.getAllExtern();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Extern> updateExtern(@PathVariable UUID id, @RequestBody Extern externDetails)
    {
        Optional<Extern> updatedExtern = externService.updateExtern(id, externDetails);
        if (updatedExtern.isPresent())
        {
            return new ResponseEntity<>(updatedExtern.get(), HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Extern not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExtern(@PathVariable UUID id)
    {
        externService.deleteExtern(id);
        return ResponseEntity.ok().build();
    }
}
