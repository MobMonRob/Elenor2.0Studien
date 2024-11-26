package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.User_PaymentInfo;
import de.dhbw.elinor2.entities.User_PaymentInfo_Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface User_PaymentInfoRepository extends JpaRepository<User_PaymentInfo, User_PaymentInfo_Id>
{
    List<User_PaymentInfo> findByUserId(UUID userId);

    Optional<User_PaymentInfo> findByUserIdAndPaymentInfoId(UUID userId, UUID paymentInfoId);

    void deleteByUserIdAndPaymentInfoId(UUID userId, UUID paymentInfoId);
}
