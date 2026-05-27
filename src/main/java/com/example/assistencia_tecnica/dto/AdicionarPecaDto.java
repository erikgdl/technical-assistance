package com.example.assistencia_tecnica.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdicionarPecaDto(
        @NotNull(message = "O ID da peça é obrigatório.")
        @Positive(message = "O ID da peça deve ser maior que zero.")
        Long pecaId,
        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade não pode ser zero ou negativa.")
        Integer quantidadeEstoque) {
}
