package de.dhbw.elinor2.controller.payments;

import de.dhbw.elinor2.services.payments.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

public abstract class PaymentController<PaymentPattern, Entity, Id> implements IPaymentController<PaymentPattern, Entity, Id>
{
    private PaymentService<PaymentPattern, Entity, Id> service;


    public PaymentController(PaymentService<PaymentPattern, Entity, Id> service)
    {
        this.service = service;
    }

    @Override
    @PostMapping("")
    public ResponseEntity<Entity> create(@RequestBody PaymentPattern paymentPattern, @AuthenticationPrincipal Jwt jwt)
    {
        Entity savedEntity = service.create(paymentPattern, jwt);
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
    public ResponseEntity<Entity> update(@PathVariable Id id, @RequestBody PaymentPattern paymentPattern, @AuthenticationPrincipal Jwt jwt)
    {
        Optional<Entity> savedEntity = service.update(id, paymentPattern, jwt);
        if (savedEntity.isPresent())
        {
            return new ResponseEntity<>(savedEntity.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Id id, @AuthenticationPrincipal Jwt jwt)
    {
        service.deleteById(id, jwt);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
