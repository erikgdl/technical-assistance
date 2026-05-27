package com.example.assistencia_tecnica.service;

import com.example.assistencia_tecnica.database.repository.IClienteRepository;
import com.example.assistencia_tecnica.database.repository.IOrdemServicoRepository;
import com.example.assistencia_tecnica.dto.DashboardResponseDto;
import com.example.assistencia_tecnica.enums.StatusServicoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IOrdemServicoRepository ordemServicoRepository;
    private final IClienteRepository clienteRepository;

    public DashboardResponseDto obterDadosDashboard() {

        long totalCliente = clienteRepository.count();
        long totalOs = ordemServicoRepository.count();

        long abertas = ordemServicoRepository.countByStatus(StatusServicoEnum.valueOf("ABERTA"));
        long emManutencao = ordemServicoRepository.countByStatus(StatusServicoEnum.valueOf("EM_MANUTENCAO"));

        return new DashboardResponseDto(totalOs, totalCliente, abertas, emManutencao);
    }

}
