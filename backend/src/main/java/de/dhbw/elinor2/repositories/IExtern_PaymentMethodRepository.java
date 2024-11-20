package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.Extern_PaymentInfo;
import de.dhbw.elinor2.entities.Extern_PaymentInfo_Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IExtern_PaymentMethodRepository extends JpaRepository<Extern_PaymentInfo, Extern_PaymentInfo_Id>
{
}
