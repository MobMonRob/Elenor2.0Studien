package de.dhbw.elinor2.services.payments.executiong;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToUser;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.payments.UserToUserRepository;
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
public class UserToUserService extends PaymentService<InputPayment, OutputPayment, UserToUser, UUID>
{
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserToUserService(UserToUserRepository repository, UserService userService, UserRepository userRepository)
    {
        super(repository);
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public UserToUser convertToEntity(InputPayment inputPaymentLight, UUID id)
    {
        User userSender = userRepository.findById(inputPaymentLight.getSenderId()).orElseThrow(()
                -> new IllegalArgumentException("User-Sender not found"));

        User userReceiver = userRepository.findById(inputPaymentLight.getReceiverId()).orElseThrow(()
                -> new IllegalArgumentException("User.Receiver not found"));

        UserToUser userToUser = new UserToUser();
        userToUser.setSender(userSender);
        userToUser.setReceiver(userReceiver);
        userToUser.setAmount(inputPaymentLight.getAmount());
        if (id != null) userToUser.setId(id);

        return userToUser;
    }

    @Override
    public void executePayment(UserToUser userToUser, Jwt jwt)
    {
        User[] users = {userToUser.getSender(), userToUser.getReceiver()};
        userService.checkUserAuthorization(users, jwt);

        User sender = userToUser.getSender();
        sender.setDebt(sender.getDebt().subtract(userToUser.getAmount()));
        userRepository.save(sender);

        User receiver = userToUser.getReceiver();
        receiver.setDebt(receiver.getDebt().add(userToUser.getAmount()));
        userRepository.save(receiver);
    }

    @Override
    public void undoPayment(UserToUser userToUser, Jwt jwt)
    {
        User[] users = {userToUser.getSender(), userToUser.getReceiver()};
        userService.checkUserAuthorization(users, jwt);

        User sender = userToUser.getSender();
        sender.setDebt(sender.getDebt().add(userToUser.getAmount()));
        userRepository.save(sender);

        User receiver = userToUser.getReceiver();
        receiver.setDebt(receiver.getDebt().subtract(userToUser.getAmount()));
        userRepository.save(receiver);
    }

    @Override
    public OutputPayment convertEntityToOutputPattern(UserToUser entity)
    {
        return new OutputPayment(
                PaymentType.User2User,
                entity.getId(),
                entity.getAmount(),
                entity.getTimestamp(),
                new TransactionEntity(
                        entity.getSender().getId(),
                        entity.getSender().getUsername()
                ),
                new TransactionEntity(
                        entity.getReceiver().getId(),
                        entity.getReceiver().getUsername()
                )
        );
    }
}
