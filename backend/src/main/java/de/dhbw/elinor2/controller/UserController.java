package de.dhbw.elinor2.controller;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.User_PaymentInfo;
import de.dhbw.elinor2.services.IGenericService;
import de.dhbw.elinor2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
public class UserController extends GenericController<User, UUID>
{

    @Autowired
    private UserService userService;

    @Autowired
    public UserController(IGenericService<User, UUID> service)
    {
        super(service);
    }

    @GetMapping("/{userId}/paymentinfos")
    public ResponseEntity<Iterable<User_PaymentInfo>> getPaymentInfos(@PathVariable UUID userId)
    {
        return ResponseEntity.ok(userService.getUserPaymentInfoFields(userId));
    }

    @GetMapping("/{userId}/paymentinfos/{paymentInfoId}")
    public ResponseEntity<User_PaymentInfo> getPaymentInfo(@PathVariable UUID userId, @PathVariable UUID paymentInfoId)
    {
        Optional<User_PaymentInfo> userPaymentInfo = userService.getUserPaymentInfoField(userId, paymentInfoId);

        if (userPaymentInfo.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Payment Info not found");

        return ResponseEntity.ok(userPaymentInfo.get());
    }

    @PostMapping("/{userId}/paymentinfos/{paymentInfoId}")
    public ResponseEntity<User_PaymentInfo> addPaymentInfo(@PathVariable UUID userId, @PathVariable UUID paymentInfoId, @RequestBody String paymentAddress)
    {
        User_PaymentInfo userPaymentInfo = userService.createUserPaymentInfo(userId, paymentInfoId, paymentAddress);
        return new ResponseEntity<>(userPaymentInfo, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/paymentinfos/{paymentInfoId}")
    public ResponseEntity<User_PaymentInfo> updatePaymentInfo(@PathVariable UUID userId, @PathVariable UUID paymentInfoId, @RequestBody String paymentAddress)
    {
        Optional<User_PaymentInfo> userPaymentInfo = userService.updateUserPaymentInfo(userId, paymentInfoId, paymentAddress);

        if (userPaymentInfo.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Payment Info not found");

        return new ResponseEntity<>(userPaymentInfo.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/paymentinfos/{paymentInfoId}")
    public void deletePaymentInfo(@PathVariable UUID userId, @PathVariable UUID paymentInfoId)
    {
        userService.deleteUserPaymentInfo(userId, paymentInfoId);
    }

}
