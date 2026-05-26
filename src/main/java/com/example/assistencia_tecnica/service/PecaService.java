package com.example.assistencia_tecnica.service;

import com.example.assistencia_tecnica.database.model.*;
import com.example.assistencia_tecnica.database.repository.IOrdemServicoRepository;
import com.example.assistencia_tecnica.database.repository.IPecaRepository;
import com.example.assistencia_tecnica.database.repository.IPecaUtilizadaRepository;
import com.example.assistencia_tecnica.dto.PecaDto;
import com.example.assistencia_tecnica.enums.StatusServicoEnum;
import com.example.assistencia_tecnica.exception.BadRequestException;
import com.example.assistencia_tecnica.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PecaService {

    private final IOrdemServicoRepository ordemServicoRepository;
    private final IPecaUtilizadaRepository pecaUtilizadaRepository;
    private final IPecaRepository pecaRepository;

    public PecaEntity criarPeca(PecaDto pecaDto) {

        return pecaRepository.save(PecaEntity.builder()
                .nome(pecaDto.getNome())
                .marca(pecaDto.getMarca())
                .quantidadeEstoque(pecaDto.getQuantidadeEstoque())
                .precoUnitario(pecaDto.getPrecoUnitario())
                .build());
    }

}
