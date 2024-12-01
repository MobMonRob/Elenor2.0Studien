package de.dhbw.elinor2.repositories;

import de.dhbw.elinor2.entities.Extern_PaymentInfo;
import de.dhbw.elinor2.entities.Extern_PaymentInfo_Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Extern_PaymentInfoRepository extends JpaRepository<Extern_PaymentInfo, Extern_PaymentInfo_Id>
{
    List<Extern_PaymentInfo> findByExternId(UUID externId);

    Optional<Extern_PaymentInfo> findByExternIdAndPaymentInfoId(UUID externId, UUID paymentInfoId);

    void deleteByExternIdAndPaymentInfoId(UUID externId, UUID paymentInfoId);
}
