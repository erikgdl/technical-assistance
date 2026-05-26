package com.example.assistencia_tecnica.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdicionarPecaDto(@NotNull @Positive(message = "A quantidade não pode ser zero ou negativa.") Long pecaId,
                               @NotNull(message = "O ID da peça é obrigatório.")  Integer quantidadeEstoque) {
}
