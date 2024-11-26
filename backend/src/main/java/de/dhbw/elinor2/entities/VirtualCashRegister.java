package de.dhbw.elinor2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "virtual_cash_register")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCashRegister
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VCRToVCR_Payment> sender_vcrToVcr_Payments;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VCRToVCR_Payment> receiver_vcrToVcr_Payments;

    @OneToMany(mappedBy = "virtualCashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserToVCR_Payment> userToVcr_Payments;

    @OneToMany(mappedBy = "virtualCashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserToExternOverVCR_Payment> userToExternOverVcr_Payments;

    @OneToMany(mappedBy = "virtualCashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ExternToUserOverVCR_Payment> externToUserOverVcr_Payments;
}
