package de.dhbw.elinor2.controller;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.entities.Extern_PaymentInfo;
import de.dhbw.elinor2.services.ExternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/externs")
public class ExternController extends GenericController<Extern, UUID>
{

    @Autowired
    private ExternService externService;

    @Autowired
    public ExternController(ExternService service)
    {
        super(service);
    }


    @GetMapping("/{externId}/paymentinfos")
    public ResponseEntity<Iterable<Extern_PaymentInfo>> getPaymentInfos(@PathVariable UUID externId)
    {
        return ResponseEntity.ok(externService.getExternPaymentInfoFields(externId));
    }

    @GetMapping("/{externId}/paymentinfos/{paymentInfoId}")
    public ResponseEntity<Extern_PaymentInfo> getPaymentInfo(@PathVariable UUID externId, @PathVariable UUID paymentInfoId)
    {
        Optional<Extern_PaymentInfo> externPaymentInfo = externService.getExternPaymentInfoField(externId, paymentInfoId);

        if (externPaymentInfo.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Extern Payment Info not found");

        return ResponseEntity.ok(externPaymentInfo.get());
    }

    @PostMapping("/{externId}/paymentinfos/{paymentInfoId}")
    public ResponseEntity<Extern_PaymentInfo> addPaymentInfo(@PathVariable UUID externId, @PathVariable UUID paymentInfoId, @RequestBody String paymentAddress)
    {
        Extern_PaymentInfo externPaymentInfo = externService.createExternPaymentInfo(externId, paymentInfoId, paymentAddress);
        return new ResponseEntity<>(externPaymentInfo, HttpStatus.CREATED);
    }

    @PutMapping("/{externId}/paymentinfos/{paymentInfoId}")
    public ResponseEntity<Extern_PaymentInfo> updatePaymentInfo(@PathVariable UUID externId, @PathVariable UUID paymentInfoId, @RequestBody String paymentAddress)
    {
        Optional<Extern_PaymentInfo> externPaymentInfo = externService.updateExternPaymentInfo(externId, paymentInfoId, paymentAddress);

        if (externPaymentInfo.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Extern Payment Info not found");

        return new ResponseEntity<>(externPaymentInfo.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{externId}/paymentinfos/{paymentInfoId}")
    public void deletePaymentInfo(@PathVariable UUID externId, @PathVariable UUID paymentInfoId)
    {
        externService.deleteExternPaymentInfo(externId, paymentInfoId);
    }
}
