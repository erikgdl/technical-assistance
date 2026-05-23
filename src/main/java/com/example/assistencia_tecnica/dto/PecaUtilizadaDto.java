package com.example.assistencia_tecnica.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PecaUtilizadaDto {

    @NotNull
    private UUID ordemServicoId;

    @NotNull
    private Long pecaId;

    @NotNull
    @Positive
    private Integer quantidade;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal precoUnitarioMomento;

}
