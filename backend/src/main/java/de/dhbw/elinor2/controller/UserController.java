package de.dhbw.elinor2.controller;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.User_PaymentInfo;
import de.dhbw.elinor2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
public class UserController
{
    private final UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/{userId}/paymentinfos")
    public ResponseEntity<Iterable<User_PaymentInfo>> getPaymentInfos(@PathVariable UUID userId)
    {
        return ResponseEntity.ok(userService.getUserPaymentInfoFields(userId));
    }

    @GetMapping("/paymentinfos")
    public ResponseEntity<Iterable<User_PaymentInfo>> getPaymentInfos()
    {
        return ResponseEntity.ok(userService.getUserPaymentInfoFields());
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
    public ResponseEntity<User_PaymentInfo> addPaymentInfo(@PathVariable UUID userId, @PathVariable UUID paymentInfoId, @RequestBody String paymentAddress, @AuthenticationPrincipal Jwt jwt)
    {
        userService.checkUserAuthorization(userId, jwt);
        User_PaymentInfo userPaymentInfo = userService.createUserPaymentInfo(userId, paymentInfoId, paymentAddress);
        return new ResponseEntity<>(userPaymentInfo, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/paymentinfos/{paymentInfoId}")
    public ResponseEntity<User_PaymentInfo> updatePaymentInfo(@PathVariable UUID userId, @PathVariable UUID paymentInfoId, @RequestBody String paymentAddress, @AuthenticationPrincipal Jwt jwt)
    {
        userService.checkUserAuthorization(userId, jwt);
        Optional<User_PaymentInfo> userPaymentInfo = userService.updateUserPaymentInfo(userId, paymentInfoId, paymentAddress);

        if (userPaymentInfo.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Payment Info not found");

        return new ResponseEntity<>(userPaymentInfo.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/paymentinfos/{paymentInfoId}")
    public void deletePaymentInfo(@PathVariable UUID userId, @PathVariable UUID paymentInfoId, @AuthenticationPrincipal Jwt jwt)
    {
        userService.checkUserAuthorization(userId, jwt);
        userService.deleteUserPaymentInfo(userId, paymentInfoId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable UUID id)
    {
        Optional<User> entity = userService.findById(id);
        if (entity.isPresent())
        {
            return new ResponseEntity<>(entity.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("")
    public ResponseEntity<Iterable<User>> findAll()
    {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id,  @AuthenticationPrincipal Jwt jwt)
    {
        userService.checkUserAuthorization(id, jwt);

        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
