package com.example.assistencia_tecnica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDto {

    @NotBlank
    @Pattern(regexp = "\\d{11}")
    @CPF
    private String cpf;

    @NotBlank
    private String nome;

    @NotBlank
    @Pattern(regexp = "^\\([1-9]{2}\\) 9?[0-9]{4}\\-[0-9]{4}$")
    private String telefone;

    @NotBlank
    @Email
    private String email;


}
