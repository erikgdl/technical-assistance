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

        Long totalCliente = clienteRepository.count();
        Long totalOs = ordemServicoRepository.count();

        Long abertas = ordemServicoRepository.countByStatus(StatusServicoEnum.valueOf("ABERTA"));
        Long emManutencao = ordemServicoRepository.countByStatus(StatusServicoEnum.valueOf("EM_MANUTENCAO"));

        return new DashboardResponseDto(totalOs, totalCliente, abertas, emManutencao);
    }

}
