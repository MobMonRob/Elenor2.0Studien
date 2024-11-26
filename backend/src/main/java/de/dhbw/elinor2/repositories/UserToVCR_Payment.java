package de.dhbw.elinor2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserToVCR_Payment extends JpaRepository<de.dhbw.elinor2.entities.UserToVCR_Payment, UUID>
{
}
