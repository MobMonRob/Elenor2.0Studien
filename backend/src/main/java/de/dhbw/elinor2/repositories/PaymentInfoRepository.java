package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, UUID>
{
}
