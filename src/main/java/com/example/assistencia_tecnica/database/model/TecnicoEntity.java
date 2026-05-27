package com.example.assistencia_tecnica.database.model;


import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tecnicos")
public class TecnicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 5)
    private String matricula;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String especialidade;

}
