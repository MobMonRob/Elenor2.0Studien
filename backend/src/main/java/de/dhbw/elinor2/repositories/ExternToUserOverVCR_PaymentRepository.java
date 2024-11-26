package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.ExternToUserOverVCR_Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExternToUserOverVCR_PaymentRepository extends JpaRepository<ExternToUserOverVCR_Payment, UUID>
{
}
