package com.example.assistencia_tecnica.service;

import com.example.assistencia_tecnica.database.model.*;
import com.example.assistencia_tecnica.database.repository.IClienteRepository;
import com.example.assistencia_tecnica.database.repository.IOrdemServicoRepository;
import com.example.assistencia_tecnica.database.repository.IServicoRealizadoRepository;
import com.example.assistencia_tecnica.database.repository.IServicoRepository;
import com.example.assistencia_tecnica.dto.ClienteDto;
import com.example.assistencia_tecnica.dto.ServicoDto;
import com.example.assistencia_tecnica.enums.StatusServicoEnum;
import com.example.assistencia_tecnica.exception.BadRequestException;
import com.example.assistencia_tecnica.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final IOrdemServicoRepository ordemServicoRepository;
    private final IServicoRepository servicoRepository;
    private final IServicoRealizadoRepository servicoRealizadoRepository;

    public ServicoEntity criarServico(ServicoDto dto) {

        return servicoRepository.save(ServicoEntity.builder()
                .descricao(dto.getDescricao())
                .precoBase(dto.getPrecoBase())
                .build());
    }



}
