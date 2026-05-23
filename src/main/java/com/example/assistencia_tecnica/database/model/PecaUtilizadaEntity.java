package com.example.assistencia_tecnica.database.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "peca_utilizada")
public class PecaUtilizadaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ordem_servico_id")
    private OrdemServicoEntity ordemServicoId;

    @ManyToOne
    @JoinColumn(name = "peca_id")
    private PecaEntity PecaId;

    private Integer quantidade;

    @Column(name = "preco_unitario_momento")
    private BigDecimal precoUnitarioMomento;



}
