package com.example.assistencia_tecnica.service;

import com.example.assistencia_tecnica.database.model.ClienteEntity;
import com.example.assistencia_tecnica.database.model.EquipamentoEntity;
import com.example.assistencia_tecnica.database.repository.IClienteRepository;
import com.example.assistencia_tecnica.database.repository.IEquipamentoRepository;
import com.example.assistencia_tecnica.dto.EquipamentoDto;
import com.example.assistencia_tecnica.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final IEquipamentoRepository equipamentoRepository;
    private final IClienteRepository clienteRepository;


    public EquipamentoEntity criarEquipamento(EquipamentoDto equipamentoDto) throws NotFoundException {
        ClienteEntity clienteEncontrado = clienteRepository.findById(equipamentoDto.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com o ID informado!"));

        return equipamentoRepository.save(EquipamentoEntity.builder()
                .tipo(equipamentoDto.getTipo())
                .marca(equipamentoDto.getMarca())
                .modelo(equipamentoDto.getModelo())
                .numeroSerie(equipamentoDto.getNumeroSerie())
                .cliente(clienteEncontrado)
                .build());
    }

    public List<EquipamentoEntity> getListarEquipamento() {
        return equipamentoRepository.findAll();
    }

    public EquipamentoEntity getBuscarPorId(UUID id) throws NotFoundException {
        return equipamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com o ID: " + id));
    }

    public Page<EquipamentoEntity> listarClientesPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return equipamentoRepository.findAll(pageable);
    }

    public List<EquipamentoEntity> buscarPorClienteId(UUID clienteId) throws NotFoundException {
        List<EquipamentoEntity> equipamentos = equipamentoRepository.findByClienteId(clienteId);
        if (equipamentos.isEmpty()) {
            throw new NotFoundException("Nenhum equipamento encontrado para o cliente ID: " + clienteId);
        }
        return equipamentos;
    }

    public EquipamentoEntity buscarPorNumeroSerie(String numeroSerie) throws NotFoundException {
        return equipamentoRepository.findByNumeroSerie(numeroSerie)
                .orElseThrow(() -> new NotFoundException("Aparelho não encontrado com o Nº de Série: " + numeroSerie));
    }

    public EquipamentoEntity atualizarEquipamento(UUID id, EquipamentoDto dto) throws NotFoundException {
        EquipamentoEntity equipamentoExistente = equipamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"));

        equipamentoExistente.setMarca(dto.getMarca());
        equipamentoExistente.setModelo(dto.getModelo());
        equipamentoExistente.setNumeroSerie(dto.getNumeroSerie());
        return equipamentoRepository.save(equipamentoExistente);
    }

}
