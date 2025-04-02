package de.dhbw.elinor2.controller.payments;

import de.dhbw.elinor2.services.payments.GeneralPaymentService;
import de.dhbw.elinor2.utils.InputPaymentOverVcr;
import de.dhbw.elinor2.utils.OutputPaymentOverVcr;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/payments")
public class GeneralPaymentController
{
    private final GeneralPaymentService service;

    public GeneralPaymentController(GeneralPaymentService service)
    {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<OutputPaymentOverVcr>> findAll()
    {
        try{
            return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutputPaymentOverVcr> findById(@PathVariable UUID id)
    {
        try{
            OutputPaymentOverVcr result = service.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt)
    {
        try{
            service.deleteById(id, jwt);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<OutputPaymentOverVcr> update(@PathVariable UUID id, @RequestBody InputPaymentOverVcr paymentPattern, @AuthenticationPrincipal Jwt jwt)
    {
        try{
            OutputPaymentOverVcr result = service.update(id, paymentPattern, jwt);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("")
    public ResponseEntity<OutputPaymentOverVcr> create(@RequestBody InputPaymentOverVcr paymentPattern, @AuthenticationPrincipal Jwt jwt)
    {
        try{
            OutputPaymentOverVcr savedEntity = service.create(paymentPattern, jwt);
            return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
