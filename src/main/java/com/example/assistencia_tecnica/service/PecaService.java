package com.example.assistencia_tecnica.service;

import com.example.assistencia_tecnica.database.model.PecaEntity;
import com.example.assistencia_tecnica.database.repository.IPecaRepository;
import com.example.assistencia_tecnica.database.repository.IPecaUtilizadaRepository;
import com.example.assistencia_tecnica.dto.PecaDto;
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
public class PecaService {

    private final IPecaRepository pecaRepository;
    private final IPecaUtilizadaRepository pecaUtilizadaRepository;

    public PecaEntity criarPeca(PecaDto pecaDto) {

        return pecaRepository.save(PecaEntity.builder()
                .nome(pecaDto.getNome())
                .marca(pecaDto.getMarca())
                .quantidadeEstoque(pecaDto.getQuantidadeEstoque())
                .precoUnitario(pecaDto.getPrecoUnitario())
                .build());
    }

    public List<PecaEntity> getListarPeca() {
        return pecaRepository.findAll();
    }

    public Page<PecaEntity> listarTodasPaginado(int page, int size) {
        Pageable paginacao = PageRequest.of(page, size, Sort.by("nome").ascending());
        return pecaRepository.findAll(paginacao);
    }

    public PecaEntity buscarPorId(Long id) throws NotFoundException {
        return pecaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Peça não encontrada no estoque com o ID: " + id));
    }

    @Transactional
    public void deletar(Long id) throws NotFoundException {
        if (!pecaRepository.existsById(id)) {
            throw new NotFoundException("Peça não encontrada no estoque com o ID: " + id);
        }

        pecaUtilizadaRepository.deleteByPecaId(id);
        pecaRepository.deleteById(id);
    }


    public PecaEntity atualizarPeca(Long id, PecaDto dto) throws NotFoundException {
        PecaEntity pecaExistente = pecaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Peça não encontrado"));

        pecaExistente.setNome(dto.getNome());
        pecaExistente.setMarca(dto.getMarca());
        pecaExistente.setPrecoUnitario(dto.getPrecoUnitario());
        pecaExistente.setQuantidadeEstoque(dto.getQuantidadeEstoque());

        return pecaRepository.save(pecaExistente);
    }

}
