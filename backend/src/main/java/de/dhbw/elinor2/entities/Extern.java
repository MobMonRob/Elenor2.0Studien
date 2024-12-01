package de.dhbw.elinor2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "extern")
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
    @JsonIgnore
    private List<Extern_PaymentInfo> extern_PaymentInfos = new ArrayList<>();

    @OneToMany(mappedBy = "extern", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserToExtern> userToExternOverVcr = new ArrayList<>();

    @OneToMany(mappedBy = "extern", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ExternToUser> externToUserOverVcr = new ArrayList<>();
}
