package de.dhbw.elinor2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="extern")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Extern
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "extern", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Extern_PaymentInfo> extern_PaymentInfos;

    @OneToMany(mappedBy = "extern", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberToExternOverVCR_Payment> memberToExternOverVcr_Payments;

    @OneToMany(mappedBy = "extern", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExternToMemberOverVCR_Payment> externToMemberOverVcr_Payments;
}
