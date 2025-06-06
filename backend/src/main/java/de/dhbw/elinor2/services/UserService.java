package de.dhbw.elinor2.services;

import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.User_PaymentInfo;
import de.dhbw.elinor2.repositories.PaymentInfoRepository;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.User_PaymentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService extends GenericService<User, UUID>
{
    private final UserRepository userRepository;
    private final User_PaymentInfoRepository userPaymentInfoRepository;
    private final PaymentInfoRepository paymentInfoRepository;

    @Autowired
    public UserService(UserRepository repository, User_PaymentInfoRepository userPaymentInfoRepository, PaymentInfoRepository paymentInfoRepository)
    {
        super(repository);
        userRepository = repository;
        this.userPaymentInfoRepository = userPaymentInfoRepository;
        this.paymentInfoRepository = paymentInfoRepository;
    }

    @Transactional(readOnly = true)
    public Iterable<User_PaymentInfo> getUserPaymentInfoFields(UUID userId)
    {
        return userPaymentInfoRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Iterable<User_PaymentInfo> getUserPaymentInfoFields()
    {
        return userPaymentInfoRepository.findAll();
    }

    @Transactional(readOnly = true)
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

    @Transactional
    public User setupUpdateUser(Jwt jwt)
    {
        UUID jwtUserId = UUID.fromString(jwt.getSubject());

        User user;

        User existingUser = userRepository.findById(jwtUserId).orElse(null);
        if(existingUser != null){
            user = existingUser;
        } else {
            user = new User();
            user.setId(jwtUserId);
        }

        user.setFirstName(jwt.getClaim("given_name"));
        user.setLastName(jwt.getClaim("family_name"));
        user.setUsername(jwt.getClaim("preferred_username"));

        userRepository.save(user);
        return user;
    }

    public void checkUserAuthorization(UUID userId, Jwt jwt)
    {
        if(!userId.equals(UUID.fromString(jwt.getSubject())))
            throw new IllegalArgumentException("User " + jwt.getClaim("preferred_username") + " not authorized to perform this action");
    }

    public void checkUserAuthorization(User[] users, Jwt jwt)
    {
        for(User user : users){
            if(user.getId().equals(UUID.fromString(jwt.getSubject())))
                return;
        }
        throw new IllegalArgumentException("User " + jwt.getClaim("preferred_username") + " not authorized to perform this action");
    }

    @Override
    protected User updateEntity(User entity, User updatedEntity)
    {
        entity.setFirstName(updatedEntity.getFirstName());
        entity.setLastName(updatedEntity.getLastName());
        entity.setUsername(updatedEntity.getUsername());
        return entity;
    }
}
