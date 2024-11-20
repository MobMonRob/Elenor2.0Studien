package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.VirtualCashRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface IVirtualCashRegisterRepository extends JpaRepository<VirtualCashRegister, UUID>
{
}
