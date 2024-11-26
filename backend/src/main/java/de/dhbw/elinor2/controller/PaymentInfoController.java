package de.dhbw.elinor2.controller;

import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.services.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/paymentinfos")
public class PaymentInfoController
{

    @Autowired
    private PaymentInfoService paymentInfoService;

    @PostMapping("")
    public ResponseEntity<PaymentInfo> createPaymentInfo(@RequestBody PaymentInfo paymentInfo)
    {
        PaymentInfo savedPaymentInfo = paymentInfoService.createPaymentInfo(paymentInfo);
        return new ResponseEntity<>(savedPaymentInfo, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentInfo> getPaymentInfoById(@PathVariable UUID id)
    {
        Optional<PaymentInfo> paymentInfo = paymentInfoService.getPaymentInfoById(id);
        if (paymentInfo.isPresent())
        {
            return new ResponseEntity<>(paymentInfo.get(), HttpStatus.OK);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PaymentInfo not found");
        }
    }

    @GetMapping("")
    public List<PaymentInfo> getAllPaymentInfo()
    {
        return paymentInfoService.getAllPaymentInfo();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentInfo> updatePaymentInfo(@PathVariable UUID id, @RequestBody PaymentInfo paymentInfoDetails)
    {
        Optional<PaymentInfo> updatedPaymentInfo = paymentInfoService.updatePaymentInfo(id, paymentInfoDetails);
        if (updatedPaymentInfo.isPresent())
        {
            return new ResponseEntity<>(updatedPaymentInfo.get(), HttpStatus.OK);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PaymentInfo not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentInfo(@PathVariable UUID id)
    {
        paymentInfoService.deletePaymentInfo(id);
        return ResponseEntity.ok().build();
    }
}
