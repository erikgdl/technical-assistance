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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final IServicoRepository servicoRepository;

    public ServicoEntity criarServico(ServicoDto dto) {

        return servicoRepository.save(ServicoEntity.builder()
                .descricao(dto.getDescricao())
                .precoBase(dto.getPrecoBase())
                .build());
    }

    public List<ServicoEntity> getListarServico() {
        return servicoRepository.findAll();
    }

    public Page<ServicoEntity> listarTodosPaginado(int page, int size) {
        Pageable paginacao = PageRequest.of(page, size, Sort.by("descricao").ascending());
        return servicoRepository.findAll(paginacao);
    }

    public ServicoEntity buscarPorId(Long id) throws NotFoundException {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado no catálogo com o ID: " + id));
    }

    public ServicoEntity atualizarServico(Long id, ServicoDto dto) throws NotFoundException {
        ServicoEntity servicoExistente = servicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado"));

        servicoExistente.setDescricao(dto.getDescricao());
        servicoExistente.setPrecoBase(dto.getPrecoBase());

        return servicoRepository.save(servicoExistente);
    }

}
