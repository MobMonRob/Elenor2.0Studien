package de.dhbw.elinor2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import de.dhbw.elinor2.entities.MemberToVCR_Payment;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface IMemberToVCR_Payment extends JpaRepository<MemberToVCR_Payment, UUID>
{
}
