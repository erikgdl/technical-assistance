package com.example.assistencia_tecnica.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistrarLaudoDto(@NotBlank(message = "O laudo técnico não pode estar em branco.") String laudoTecnico) {
}
