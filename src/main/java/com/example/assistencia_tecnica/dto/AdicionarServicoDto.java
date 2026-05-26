package com.example.assistencia_tecnica.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdicionarServicoDto(@NotNull @Positive(message = "O ID do serviço é obrigatório.") Long id) {
}
