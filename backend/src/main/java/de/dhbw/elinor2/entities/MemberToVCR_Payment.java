package de.dhbw.elinor2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="member_to_vcr_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberToVCR_Payment extends Payment
{
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "vcr_id", nullable = false)
    private VirtualCashRegister virtualCashRegister;
}
