package de.dhbw.elinor2.services.payments.executiong;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToExtern;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.ExternRepository;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.UserToExternRepository;
import de.dhbw.elinor2.services.UserService;
import de.dhbw.elinor2.services.payments.PaymentService;
import de.dhbw.elinor2.utils.InputPaymentOverVcr;
import de.dhbw.elinor2.utils.OutputPaymentOverVcr;
import de.dhbw.elinor2.utils.PaymentType;
import de.dhbw.elinor2.utils.TransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserToExternService extends PaymentService<InputPaymentOverVcr, OutputPaymentOverVcr, UserToExtern, UUID>
{
    private final ExternRepository externRepository;
    private final UserRepository userRepository;
    private final VirtualCashRegisterRepository virtualCashRegisterRepository;

    private final UserService userService;

    @Autowired
    public UserToExternService(UserToExternRepository repository,
                               UserService userService,
                               ExternRepository externRepository,
                               UserRepository userRepository,
                               VirtualCashRegisterRepository virtualCashRegisterRepository)
    {
        super(repository);
        this.userService = userService;
        this.externRepository = externRepository;
        this.userRepository = userRepository;
        this.virtualCashRegisterRepository = virtualCashRegisterRepository;
    }

    @Override
    public UserToExtern convertToEntity(InputPaymentOverVcr paymentOverVCRLight, UUID id)
    {
        User userSender = userRepository.findById(paymentOverVCRLight.getSenderId()).orElseThrow(()
                -> new IllegalArgumentException("User not found"));

        Extern externReceiver = externRepository.findById(paymentOverVCRLight.getReceiverId()).orElseThrow(()
                -> new IllegalArgumentException("Extern not found"));

        VirtualCashRegister vcr = virtualCashRegisterRepository.findById(paymentOverVCRLight.getVcrId()).orElseThrow(()
                -> new IllegalArgumentException("VirtualCashRegister not found"));

        UserToExtern userToExtern = new UserToExtern();
        userToExtern.setUser(userSender);
        userToExtern.setExtern(externReceiver);
        userToExtern.setAmount(paymentOverVCRLight.getAmount());
        userToExtern.setVirtualCashRegister(vcr);
        if (id != null) userToExtern.setId(id);

        return userToExtern;
    }

    @Override
    public void executePayment(UserToExtern userToExtern, Jwt jwt)
    {
        userService.checkUserAuthorization(userToExtern.getUser().getId(), jwt);

        User user = userToExtern.getUser();
        user.setDebt(user.getDebt().subtract(userToExtern.getAmount()));
        userRepository.save(user);

        VirtualCashRegister vcr = userToExtern.getVirtualCashRegister();
        vcr.setBalance(vcr.getBalance().subtract(userToExtern.getAmount()));
        virtualCashRegisterRepository.save(vcr);
    }

    @Override
    public void undoPayment(UserToExtern userToExtern, Jwt jwt)
    {
        userService.checkUserAuthorization(userToExtern.getUser().getId(), jwt);

        User user = userToExtern.getUser();
        user.setDebt(user.getDebt().add(userToExtern.getAmount()));
        userRepository.save(user);

        VirtualCashRegister vcr = userToExtern.getVirtualCashRegister();
        vcr.setBalance(vcr.getBalance().add(userToExtern.getAmount()));
        virtualCashRegisterRepository.save(vcr);
    }

    @Override
    public OutputPaymentOverVcr convertEntityToOutputPattern(UserToExtern entity)
    {
        return new OutputPaymentOverVcr(
                PaymentType.User2ExternOverVcr,
                entity.getId(),
                entity.getAmount(),
                entity.getTimestamp(),
                new TransactionEntity(
                        entity.getUser().getId(),
                        entity.getUser().getUsername()
                ),
                new TransactionEntity(
                        entity.getExtern().getId(),
                        entity.getExtern().getName()
                ),
                new TransactionEntity(
                        entity.getVirtualCashRegister().getId(),
                        entity.getVirtualCashRegister().getName()
                )
        );
    }
}
