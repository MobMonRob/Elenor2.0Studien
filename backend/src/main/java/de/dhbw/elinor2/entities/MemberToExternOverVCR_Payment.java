package de.dhbw.elinor2.entities;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="member_to_extern_over_vcr_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberToExternOverVCR_Payment extends Payment
{
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "vcr_id", nullable = false)
    private VirtualCashRegister virtualCashRegister;

    @ManyToOne
    @JoinColumn(name = "extern_id", nullable = false)
    private Extern extern;
}
