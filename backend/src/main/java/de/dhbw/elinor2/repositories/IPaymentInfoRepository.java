package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentInfoRepository extends JpaRepository<PaymentInfo, UUID>
{
}
