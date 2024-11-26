package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.VirtualCashRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VirtualCashRegisterRepository extends JpaRepository<VirtualCashRegister, UUID>
{
}
