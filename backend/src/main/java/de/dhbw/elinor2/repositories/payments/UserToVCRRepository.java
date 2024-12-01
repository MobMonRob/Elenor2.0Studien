package de.dhbw.elinor2.repositories.payments;

import de.dhbw.elinor2.entities.UserToVCR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserToVCRRepository extends JpaRepository<UserToVCR, UUID>
{
}
