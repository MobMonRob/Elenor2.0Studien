package de.dhbw.elinor2.services.payments.executiong;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToUser;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.payments.UserToUserRepository;
import de.dhbw.elinor2.services.payments.PaymentService;
import de.dhbw.elinor2.utils.PaymentLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserToUserService extends PaymentService<PaymentLight, UserToUser, UUID>
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserToUserService(UserToUserRepository repository)
    {
        super(repository);
    }

    @Override
    public UserToUser convertToEntity(PaymentLight paymentLight, UUID id)
    {
        User userSender = userRepository.findById(paymentLight.getSenderId()).orElseThrow(()
                -> new IllegalArgumentException("User-Sender not found"));

        User userReceiver = userRepository.findById(paymentLight.getReceiverId()).orElseThrow(()
                -> new IllegalArgumentException("User.Receiver not found"));

        UserToUser userToUser = new UserToUser();
        userToUser.setSender(userSender);
        userToUser.setReceiver(userReceiver);
        userToUser.setAmount(paymentLight.getAmount());
        if (id != null) userToUser.setId(id);

        return userToUser;
    }

    @Override
    public void executePayment(UserToUser userToUser)
    {
        User sender = userToUser.getSender();
        sender.setDebt(sender.getDebt().subtract(userToUser.getAmount()));
        userRepository.save(sender);

        User receiver = userToUser.getReceiver();
        receiver.setDebt(receiver.getDebt().add(userToUser.getAmount()));
        userRepository.save(receiver);
    }

    @Override
    public void undoPayment(UserToUser userToUser)
    {
        User sender = userToUser.getSender();
        sender.setDebt(sender.getDebt().add(userToUser.getAmount()));
        userRepository.save(sender);

        User receiver = userToUser.getReceiver();
        receiver.setDebt(receiver.getDebt().subtract(userToUser.getAmount()));
        userRepository.save(receiver);
    }
}
