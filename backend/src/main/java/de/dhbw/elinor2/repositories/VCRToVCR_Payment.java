package de.dhbw.elinor2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VCRToVCR_Payment extends JpaRepository<de.dhbw.elinor2.entities.VCRToVCR_Payment, UUID>
{
}
