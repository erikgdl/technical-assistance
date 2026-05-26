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

    @NotNull(message = "O ID do cliente é obrigatório")
    private UUID clienteId;

    @NotNull(message = "O ID do equipamento é obrigatório")
    private UUID equipamentoId;

    @NotBlank(message = "O defeito relatado não pode estar vazio")
    private String defeitoRelatado;

}
