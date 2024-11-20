package de.dhbw.elinor2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import de.dhbw.elinor2.entities.MemberToMember_Payment;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface IMemberToMember_PaymentRepository extends JpaRepository<MemberToMember_Payment, UUID>
{
}
