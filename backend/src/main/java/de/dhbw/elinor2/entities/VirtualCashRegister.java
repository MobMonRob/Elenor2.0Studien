package de.dhbw.elinor2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VCRToVCR> sender_vcrToVcr = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VCRToVCR> receiver_vcrToVcr = new ArrayList<>();

    @OneToMany(mappedBy = "virtualCashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserToVCR> userToVcr = new ArrayList<>();

    @OneToMany(mappedBy = "virtualCashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VCRToUser> vcrToUser = new ArrayList<>();

    @OneToMany(mappedBy = "virtualCashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserToExtern> userToExtern = new ArrayList<>();

    @OneToMany(mappedBy = "virtualCashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ExternToUser> externToUser = new ArrayList<>();
}
