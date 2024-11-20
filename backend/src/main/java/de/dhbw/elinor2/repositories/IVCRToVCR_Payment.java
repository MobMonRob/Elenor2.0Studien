package de.dhbw.elinor2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import de.dhbw.elinor2.entities.VCRToVCR_Payment;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface IVCRToVCR_Payment extends JpaRepository<VCRToVCR_Payment, UUID>
{
}
