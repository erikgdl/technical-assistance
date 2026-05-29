package com.example.assistencia_tecnica.service;

import com.example.assistencia_tecnica.database.model.ServicoEntity;
import com.example.assistencia_tecnica.database.repository.IServicoRealizadoRepository;
import com.example.assistencia_tecnica.database.repository.IServicoRepository;
import com.example.assistencia_tecnica.dto.ServicoDto;
import com.example.assistencia_tecnica.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final IServicoRepository servicoRepository;
    private final IServicoRealizadoRepository servicoRealizadoRepository;

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

    @Transactional
    public void deletar(Long id) throws NotFoundException {
        if (!servicoRepository.existsById(id)) {
            throw new NotFoundException("Serviço não encontrado no catálogo com o ID: " + id);
        }

        servicoRealizadoRepository.deleteByServicoId_Id(id);
        servicoRepository.deleteById(id);
    }

    public ServicoEntity atualizarServico(Long id, ServicoDto dto) throws NotFoundException {
        ServicoEntity servicoExistente = servicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado"));

        servicoExistente.setDescricao(dto.getDescricao());
        servicoExistente.setPrecoBase(dto.getPrecoBase());

        return servicoRepository.save(servicoExistente);
    }

}
