package de.dhbw.elinor2.controller.payments;

import de.dhbw.elinor2.services.payments.GeneralPaymentService;
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
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
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
}
