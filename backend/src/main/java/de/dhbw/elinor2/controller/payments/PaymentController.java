package de.dhbw.elinor2.controller.payments;

import de.dhbw.elinor2.services.payments.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

public abstract class PaymentController<PaymentPattern, T, ID> implements IPaymentController<PaymentPattern, T, ID>
{
    private final PaymentService<PaymentPattern, T, ID> service;

    protected PaymentController(PaymentService<PaymentPattern, T, ID> service)
    {
        this.service = service;
    }

    @Override
    @PostMapping("")
    public ResponseEntity<T> create(@RequestBody PaymentPattern paymentPattern, @AuthenticationPrincipal Jwt jwt)
    {
        T savedEntity = service.create(paymentPattern, jwt);
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
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody PaymentPattern paymentPattern, @AuthenticationPrincipal Jwt jwt)
    {
        Optional<T> savedEntity = service.update(id, paymentPattern, jwt);
        if (savedEntity.isPresent())
        {
            return new ResponseEntity<>(savedEntity.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable ID id, @AuthenticationPrincipal Jwt jwt)
    {
        service.deleteById(id, jwt);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
