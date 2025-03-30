package de.dhbw.elinor2.controller.payments;

import de.dhbw.elinor2.services.payments.PaymentService;
import de.dhbw.elinor2.utils.InputPayment;
import de.dhbw.elinor2.utils.OutputPayment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

public abstract class PaymentController<IP extends InputPayment, OP extends OutputPayment, T, ID> implements IPaymentController<IP, OP, T, ID>
{
    private final PaymentService<IP, OP, T, ID> service;

    protected PaymentController(PaymentService<IP, OP, T, ID> service)
    {
        this.service = service;
    }

    @Override
    @PostMapping("")
    public ResponseEntity<OP> create(@RequestBody IP paymentPattern, @AuthenticationPrincipal Jwt jwt)
    {
        OP savedEntity = service.create(paymentPattern, jwt);
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
    public ResponseEntity<Iterable<OP>> findAll()
    {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<OP> update(@PathVariable ID id, @RequestBody IP paymentPattern, @AuthenticationPrincipal Jwt jwt)
    {
        return new ResponseEntity<>(service.update(id, paymentPattern, jwt), HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable ID id, @AuthenticationPrincipal Jwt jwt)
    {
        service.deleteById(id, jwt);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
