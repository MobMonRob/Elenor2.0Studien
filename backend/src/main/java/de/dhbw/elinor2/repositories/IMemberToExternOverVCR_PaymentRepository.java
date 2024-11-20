package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.MemberToExternOverVCR_Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface IMemberToExternOverVCR_PaymentRepository extends JpaRepository<MemberToExternOverVCR_Payment, UUID>
{
}
