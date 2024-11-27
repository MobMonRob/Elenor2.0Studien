package de.dhbw.elinor2.services;

import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.User_PaymentInfo;
import de.dhbw.elinor2.repositories.PaymentInfoRepository;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.User_PaymentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService
{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private User_PaymentInfoRepository userPaymentInfoRepository;
    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    @Transactional
    public User createUser(User user)
    {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(UUID userId)
    {
        return userRepository.findById(userId);
    }

    public Iterable<User> getUsers()
    {
        return userRepository.findAll();
    }

    @Transactional
    public Optional<User> updateUser(UUID userId, User userDetails)
    {
        return userRepository.findById(userId).map(user ->
        {
            user.setUsername(userDetails.getUsername());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setDebt(userDetails.getDebt());
            return userRepository.save(user);
        });
    }

    @Transactional
    public void deleteUser(UUID userId)
    {
        userRepository.deleteById(userId);
    }

    @Transactional
    public Iterable<User_PaymentInfo> getUserPaymentInfoFields(UUID userId)
    {
        return userPaymentInfoRepository.findByUserId(userId);
    }

    @Transactional
    public Optional<User_PaymentInfo> getUserPaymentInfoField(UUID userId, UUID paymentInfoId)
    {
        return userPaymentInfoRepository.findByUserIdAndPaymentInfoId(userId, paymentInfoId);
    }

    @Transactional
    public User_PaymentInfo createUserPaymentInfo(UUID userId, UUID paymentInfoId, String paymentAddress)
    {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new IllegalArgumentException("User not found"));
        PaymentInfo paymentInfo = paymentInfoRepository.findById(paymentInfoId).orElseThrow(()
                -> new IllegalArgumentException("PaymentInfo not found"));

        User_PaymentInfo userPaymentInfo = new User_PaymentInfo();
        userPaymentInfo.setUser(user);
        userPaymentInfo.setPaymentInfo(paymentInfo);
        userPaymentInfo.setPaymentAddress(paymentAddress);
        userPaymentInfoRepository.save(userPaymentInfo);
        return userPaymentInfo;
    }

    @Transactional
    public Optional<User_PaymentInfo> updateUserPaymentInfo(UUID userId, UUID paymentInfoId, String paymentAddress)
    {
        return userPaymentInfoRepository.findByUserIdAndPaymentInfoId(userId, paymentInfoId).map(userPaymentInfo ->
        {
            userPaymentInfo.setPaymentAddress(paymentAddress);
            return userPaymentInfoRepository.save(userPaymentInfo);
        });
    }

    @Transactional
    public void deleteUserPaymentInfo(UUID userId, UUID paymentInfoId)
    {
        userPaymentInfoRepository.deleteByUserIdAndPaymentInfoId(userId, paymentInfoId);
    }
}
