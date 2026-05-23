package com.example.assistencia_tecnica.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TecnicoDto {

    @NotBlank
    private String matricula;

    @NotBlank
    private String nome;

    @NotBlank
    private String especialidade;

}
