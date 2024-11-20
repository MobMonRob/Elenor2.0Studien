package de.dhbw.elinor2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Actor
{
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private BigDecimal debt = BigDecimal.ZERO;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberToMember_Payment> sender_MemberToMember_Payments;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberToMember_Payment> receiver_MemberToMember_Payments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberToVCR_Payment> memberToVcr_Payments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberToExternOverVCR_Payment> memberToExternOverVcr_Payments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExternToMemberOverVCR_Payment> externToMemberOverVcr_Payments;
}
