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
@Table(name = "user_to_extern_over_vcr_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserToExtern extends Payment
{
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "vcr_id", nullable = false)
    private VirtualCashRegister virtualCashRegister;

    @ManyToOne
    @JoinColumn(name = "extern_id", nullable = false)
    private Extern extern;
}
