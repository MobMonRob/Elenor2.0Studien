package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.Member_PaymentInfo;
import de.dhbw.elinor2.entities.Member_PaymentInfo_Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMember_PaymentMethodRepository extends JpaRepository<Member_PaymentInfo, Member_PaymentInfo_Id>
{
}
