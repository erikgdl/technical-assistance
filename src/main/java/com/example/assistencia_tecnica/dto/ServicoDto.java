package com.example.assistencia_tecnica.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicoDto {

    private Long id;

    @NotBlank
    private String descricao;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal precoBase;

}
