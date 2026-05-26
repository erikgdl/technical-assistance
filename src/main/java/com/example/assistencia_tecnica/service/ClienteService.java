package com.example.assistencia_tecnica.service;

import com.example.assistencia_tecnica.database.model.ClienteEntity;
import com.example.assistencia_tecnica.database.repository.IClienteRepository;
import com.example.assistencia_tecnica.dto.ClienteDto;
import com.example.assistencia_tecnica.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final IClienteRepository clienteRepository;

    public ClienteEntity criarCliente(ClienteDto clienteDto) throws BadRequestException {
        ClienteEntity clienteEmail = clienteRepository.findByEmail(clienteDto.getEmail())
                .orElse(null);

        ClienteEntity clienteCpf = clienteRepository.findByCpf(clienteDto.getCpf())
                .orElse(null);

        if (clienteEmail != null) {
            throw new BadRequestException("Aluno já cadastrado com este e-mail");
        }

        if (clienteCpf != null) {
            throw new BadRequestException("Aluno já cadastrado com este Cpf");
        }

        return clienteRepository.save(ClienteEntity.builder()
                .cpf(clienteDto.getCpf())
                .nome(clienteDto.getNome())
                .telefone(clienteDto.getTelefone())
                .email(clienteDto.getEmail())
                .build());
    }



}
