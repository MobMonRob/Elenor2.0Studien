package de.dhbw.elinor2.services.payments;

import de.dhbw.elinor2.services.payments.documenting.UserToVCRService;
import de.dhbw.elinor2.services.payments.documenting.VCRToUserService;
import de.dhbw.elinor2.services.payments.documenting.VCRToVCRService;
import de.dhbw.elinor2.services.payments.executiong.ExternToUserService;
import de.dhbw.elinor2.services.payments.executiong.UserToExternService;
import de.dhbw.elinor2.services.payments.executiong.UserToUserService;
import de.dhbw.elinor2.utils.InputPaymentOverVcr;
import de.dhbw.elinor2.utils.OutputPayment;
import de.dhbw.elinor2.utils.OutputPaymentOverVcr;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class GeneralPaymentService
{
    private final UserToVCRService userToVCRService;
    private final VCRToUserService vcrToUserService;
    private final VCRToVCRService vcrToVCRService;
    private final ExternToUserService externToUserService;
    private final UserToExternService userToExternService;
    private final UserToUserService userToUserService;

    public GeneralPaymentService(UserToVCRService userToVCRService,
                                 VCRToUserService vcrToUserService,
                                 VCRToVCRService vcrToVCRService,
                                 ExternToUserService externToUserService,
                                 UserToExternService userToExternService,
                                 UserToUserService userToUserService)
    {
        this.userToVCRService = userToVCRService;
        this.vcrToUserService = vcrToUserService;
        this.vcrToVCRService = vcrToVCRService;
        this.externToUserService = externToUserService;
        this.userToExternService = userToExternService;
        this.userToUserService = userToUserService;
    }

    public Iterable<OutputPaymentOverVcr> findAll()
    {
        List<OutputPaymentOverVcr> result = new ArrayList<>();
        result.addAll(externToUserService.findAll());
        result.addAll(userToExternService.findAll());
        result.addAll(convertToOutputPaymentOverVcr(userToVCRService.findAll()));
        result.addAll(convertToOutputPaymentOverVcr(vcrToUserService.findAll()));
        result.addAll(convertToOutputPaymentOverVcr(vcrToVCRService.findAll()));
        result.addAll(convertToOutputPaymentOverVcr(userToUserService.findAll()));

        result.sort((item1, item2) -> item2.getTimestamp().compareTo(item1.getTimestamp()));

        return result;
    }

    public void deleteById(UUID id, Jwt jwt)
    {
        if(externToUserService.existsById(id))
            externToUserService.deleteById(id, jwt);
        else if (userToUserService.existsById(id))
            userToUserService.deleteById(id, jwt);
        else if (vcrToUserService.existsById(id))
            vcrToUserService.deleteById(id, jwt);
        else if (userToVCRService.existsById(id))
            userToVCRService.deleteById(id, jwt);
        else if (vcrToVCRService.existsById(id))
            vcrToVCRService.deleteById(id, jwt);
        else if (userToExternService.existsById(id))
            userToExternService.deleteById(id, jwt);
        else
            throw new IllegalArgumentException("Entity not found");
    }

    public OutputPaymentOverVcr update(UUID id, InputPaymentOverVcr paymentPattern, Jwt jwt)
    {
        OutputPaymentOverVcr updatedPayment;
        if(externToUserService.existsById(id))
            updatedPayment = externToUserService.update(id, paymentPattern, jwt);
        else if (userToUserService.existsById(id))
            updatedPayment = convertToOutputPaymentOverVcr(userToUserService.update(id, paymentPattern, jwt));
        else if (vcrToUserService.existsById(id))
            updatedPayment = convertToOutputPaymentOverVcr(vcrToUserService.update(id, paymentPattern, jwt));
        else if (userToVCRService.existsById(id))
            updatedPayment = convertToOutputPaymentOverVcr(userToVCRService.update(id, paymentPattern, jwt));
        else if (vcrToVCRService.existsById(id))
            updatedPayment = convertToOutputPaymentOverVcr(vcrToVCRService.update(id, paymentPattern, jwt));
        else if (userToExternService.existsById(id))
            updatedPayment = userToExternService.update(id, paymentPattern, jwt);
        else
            throw new IllegalArgumentException("Entity not found");
        return updatedPayment;
    }

    private List<OutputPaymentOverVcr> convertToOutputPaymentOverVcr(Collection<OutputPayment> outputPayments)
    {
        List<OutputPaymentOverVcr> result = new ArrayList<>();
        for(OutputPayment outputPayment : outputPayments)
        {
            result.add(convertToOutputPaymentOverVcr(outputPayment));
        }
        return result;
    }

    private OutputPaymentOverVcr convertToOutputPaymentOverVcr(OutputPayment outputPayment)
    {
        return new OutputPaymentOverVcr(
                outputPayment.getPaymentType(),
                outputPayment.getTransactionId(),
                outputPayment.getAmount(),
                outputPayment.getTimestamp(),
                outputPayment.getSender(),
                outputPayment.getReceiver(),
                null
        );
    }


}
