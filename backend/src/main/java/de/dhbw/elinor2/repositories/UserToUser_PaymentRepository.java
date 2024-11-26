package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.UserToUser_Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserToUser_PaymentRepository extends JpaRepository<UserToUser_Payment, UUID>
{
}
