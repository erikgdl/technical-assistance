package com.example.assistencia_tecnica.database.model;

import com.example.assistencia_tecnica.enums.StatusServicoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "ordem_servico")
public class OrdemServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "defeito_relatado", nullable = false, columnDefinition = "TEXT")
    private String defeitoRelatado;

    @Column(name = "laudo_tecnico", columnDefinition = "TEXT")
    private String laudoTecnico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusServicoEnum status;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(name = "valor_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotal;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private ClienteEntity cliente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id")
    private EquipamentoEntity equipamento;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private TecnicoEntity tecnico;

}
