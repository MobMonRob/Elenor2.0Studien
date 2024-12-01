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
@Table(name = "vcr_to_vcr_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VCRToVCR extends Payment
{
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private VirtualCashRegister sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private VirtualCashRegister receiver;
}
