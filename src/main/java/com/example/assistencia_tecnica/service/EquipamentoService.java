package com.example.assistencia_tecnica.service;

import com.example.assistencia_tecnica.database.model.ClienteEntity;
import com.example.assistencia_tecnica.database.model.EquipamentoEntity;
import com.example.assistencia_tecnica.database.repository.IClienteRepository;
import com.example.assistencia_tecnica.database.repository.IEquipamentoRepository;
import com.example.assistencia_tecnica.dto.EquipamentoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final IEquipamentoRepository equipamentoRepository;
    private final IClienteRepository clienteRepository;


    public EquipamentoEntity criarEquipamento(EquipamentoDto equipamentoDto) {
        ClienteEntity clienteEncontrado = clienteRepository.findById(equipamentoDto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID informado!"));

        return equipamentoRepository.save(EquipamentoEntity.builder()
                .tipo(equipamentoDto.getTipo())
                .marca(equipamentoDto.getMarca())
                .modelo(equipamentoDto.getModelo())
                .numeroSerie(equipamentoDto.getNumeroSerie())
                .cliente(clienteEncontrado)
                .build());
    }

}
