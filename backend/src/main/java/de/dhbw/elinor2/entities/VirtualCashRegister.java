package de.dhbw.elinor2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="virtual_cash_register")
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
    private List<VCRToVCR_Payment> sender_vcrToVcr_Payments;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VCRToVCR_Payment> receiver_vcrToVcr_Payments;

    @OneToMany(mappedBy = "virtualCashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberToVCR_Payment> memberToVcr_Payments;

    @OneToMany(mappedBy = "virtualCashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberToExternOverVCR_Payment> memberToExternOverVcr_Payments;

    @OneToMany(mappedBy = "virtualCashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExternToMemberOverVCR_Payment> externToMemberOverVcr_Payments;
}
