package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.ExternToMemberOverVCR_Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface IExternToMemberOverVCR_PaymentRepository extends JpaRepository<ExternToMemberOverVCR_Payment, UUID>
{
}
