package com.example.assistencia_tecnica.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cliente")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cpf", unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private Set<EquipamentoEntity> equipamentos = new HashSet<>();

}
