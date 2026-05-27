package com.example.assistencia_tecnica.dto;

public record DashboardResponseDto(Long totalOS,
                                   Long totalCliente,
                                   Long totalOSAberta,
                                   Long totalOSEmManutencao) {
}
