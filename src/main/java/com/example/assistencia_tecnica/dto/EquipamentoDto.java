package com.example.assistencia_tecnica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipamentoDto {


    @NotNull(message = "O ID do cliente/aluno é obrigatório")
    private UUID clienteId;

    @NotBlank
    private String tipo;

    @NotBlank
    private String marca;

    @NotBlank
    private String modelo;

    @NotBlank
    private String numeroSerie;


}
