package com.example.assistencia_tecnica.database.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "servico_Realizado",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ordem_servico_id", "peca_id"})
)
public class ServicoRealizadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonBackReference("ordem-itens")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServicoEntity ordemServico;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "peca_id", nullable = false)
    private PecaEntity peca;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario_momento", nullable = false, precision = 19, scale = 2)
    private BigDecimal precoUnitarioMomento;

}
