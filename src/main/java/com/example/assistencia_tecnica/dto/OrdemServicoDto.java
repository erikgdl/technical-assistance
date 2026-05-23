package com.example.assistencia_tecnica.dto;

import com.example.assistencia_tecnica.enums.StatusServicoEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdemServicoDto {


    @NotBlank
    private String defeitoRelatado;

    @NotBlank
    private String laudoTecnico;

    @NotNull
    private StatusServicoEnum status;

    @NotNull
    private LocalDateTime dataAbertura;

    private LocalDateTime dataConclusao;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal valorTotal;

    @NotNull
    private UUID clienteId;

    @NotNull
    private UUID equipamentoId;

    @NotNull
    private Long tecnicoId;

}
