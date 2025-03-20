package de.dhbw.elinor2.services.payments.documenting;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.VCRToUser;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.VCRToUserRepository;
import de.dhbw.elinor2.services.UserService;
import de.dhbw.elinor2.services.payments.PaymentService;
import de.dhbw.elinor2.utils.InputPayment;
import de.dhbw.elinor2.utils.OutputPayment;
import de.dhbw.elinor2.utils.PaymentType;
import de.dhbw.elinor2.utils.TransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VCRToUserService extends PaymentService<InputPayment, OutputPayment, VCRToUser, UUID>
{
    private final UserRepository userRepository;
    private final VirtualCashRegisterRepository virtualCashRegisterRepository;

    private final UserService userService;

    @Autowired
    public VCRToUserService(VCRToUserRepository repository,
                            UserService userService,
                            UserRepository userRepository,
                            VirtualCashRegisterRepository virtualCashRegisterRepository)
    {
        super(repository);
        this.userService = userService;
        this.userRepository = userRepository;
        this.virtualCashRegisterRepository = virtualCashRegisterRepository;
    }

    @Override
    public VCRToUser convertToEntity(InputPayment inputPaymentLight, UUID id)
    {
        VirtualCashRegister sender = virtualCashRegisterRepository.findById(inputPaymentLight.getSenderId()).orElseThrow(()
                -> new IllegalArgumentException("Sender not found"));

        User receiver = userRepository.findById(inputPaymentLight.getReceiverId()).orElseThrow(()
                -> new IllegalArgumentException("Receiver not found"));

        VCRToUser vcrToUser = new VCRToUser();
        vcrToUser.setAmount(inputPaymentLight.getAmount());
        vcrToUser.setVirtualCashRegister(sender);
        vcrToUser.setUser(receiver);
        if (id != null) vcrToUser.setId(id);
        return vcrToUser;
    }

    @Override
    public void executePayment(VCRToUser vcrToUser, Jwt jwt)
    {
        userService.checkUserAuthorization(vcrToUser.getUser().getId(), jwt);

        VirtualCashRegister sender = vcrToUser.getVirtualCashRegister();
        User receiver = vcrToUser.getUser();
        sender.setBalance(sender.getBalance().subtract(vcrToUser.getAmount()));
        receiver.setDebt(receiver.getDebt().subtract(vcrToUser.getAmount()));
        virtualCashRegisterRepository.save(sender);
        userRepository.save(receiver);
    }

    @Override
    public void undoPayment(VCRToUser vcrToUser, Jwt jwt)
    {
        userService.checkUserAuthorization(vcrToUser.getUser().getId(), jwt);

        VirtualCashRegister sender = vcrToUser.getVirtualCashRegister();
        User receiver = vcrToUser.getUser();
        sender.setBalance(sender.getBalance().add(vcrToUser.getAmount()));
        receiver.setDebt(receiver.getDebt().add(vcrToUser.getAmount()));
        virtualCashRegisterRepository.save(sender);
        userRepository.save(receiver);
    }

    @Override
    public OutputPayment convertEntityToOutputPattern(VCRToUser entity)
    {
        return new OutputPayment(
                PaymentType.Vcr2User,
                entity.getId(),
                entity.getAmount(),
                entity.getTimestamp(),
                new TransactionEntity(
                        entity.getVirtualCashRegister().getId(),
                        entity.getVirtualCashRegister().getName()
                ),
                new TransactionEntity(
                        entity.getUser().getId(),
                        entity.getUser().getUsername()
                )
        );
    }
}
