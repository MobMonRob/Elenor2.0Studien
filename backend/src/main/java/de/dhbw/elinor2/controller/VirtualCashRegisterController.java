package de.dhbw.elinor2.controller;

import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.services.VirtualCashRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/virtualcashregisters")
public class VirtualCashRegisterController
{

    @Autowired
    private VirtualCashRegisterService virtualCashRegisterService;

    @PostMapping("")
    public ResponseEntity<VirtualCashRegister> createVirtualCashRegister(@RequestBody VirtualCashRegister virtualCashRegister)
    {
        VirtualCashRegister savedVirtualCashRegister = virtualCashRegisterService.createVirtualCashRegister(virtualCashRegister);
        return new ResponseEntity<>(savedVirtualCashRegister, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VirtualCashRegister> getVirtualCashRegisterById(@PathVariable UUID id)
    {
        Optional<VirtualCashRegister> virtualCashRegister = virtualCashRegisterService.getVirtualCashRegisterById(id);
        if (virtualCashRegister.isPresent())
        {
            return new ResponseEntity<>(virtualCashRegister.get(), HttpStatus.OK);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VirtualCashRegister not found");
        }
    }

    @GetMapping("")
    public List<VirtualCashRegister> getAllVirtualCashRegisters()
    {
        return virtualCashRegisterService.getAllVirtualCashRegisters();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VirtualCashRegister> updateVirtualCashRegister(@PathVariable UUID id, @RequestBody VirtualCashRegister virtualCashRegisterDetails)
    {
        Optional<VirtualCashRegister> updatedVirtualCashRegister = virtualCashRegisterService.updateVirtualCashRegister(id, virtualCashRegisterDetails);
        if (updatedVirtualCashRegister.isPresent())
        {
            return new ResponseEntity<>(updatedVirtualCashRegister.get(), HttpStatus.OK);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VirtualCashRegister not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVirtualCashRegister(@PathVariable UUID id)
    {
        virtualCashRegisterService.deleteVirtualCashRegister(id);
        return ResponseEntity.ok().build();
    }
}
