package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.UserToExternOverVCR_Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserToExternOverVCR_PaymentRepository extends JpaRepository<UserToExternOverVCR_Payment, UUID>
{
}
