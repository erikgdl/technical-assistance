package com.example.assistencia_tecnica.service;

import com.example.assistencia_tecnica.database.model.ClienteEntity;
import com.example.assistencia_tecnica.database.model.OrdemServicoEntity;
import com.example.assistencia_tecnica.database.repository.IClienteRepository;
import com.example.assistencia_tecnica.database.repository.IEquipamentoRepository;
import com.example.assistencia_tecnica.database.repository.IOrdemServicoRepository;
import com.example.assistencia_tecnica.database.repository.IPecaUtilizadaRepository;
import com.example.assistencia_tecnica.database.repository.IServicoRealizadoRepository;
import com.example.assistencia_tecnica.dto.ClienteDto;
import com.example.assistencia_tecnica.exception.BadRequestException;
import com.example.assistencia_tecnica.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final IClienteRepository clienteRepository;
    private final IEquipamentoRepository equipamentoRepository;
    private final IOrdemServicoRepository ordemServicoRepository;
    private final IPecaUtilizadaRepository pecaUtilizadaRepository;
    private final IServicoRealizadoRepository servicoRealizadoRepository;

    public ClienteEntity criarCliente(ClienteDto clienteDto) throws BadRequestException {
        ClienteEntity clienteEmail = clienteRepository.findByEmail(clienteDto.getEmail())
                .orElse(null);

        ClienteEntity clienteCpf = clienteRepository.findByCpf(clienteDto.getCpf())
                .orElse(null);

        if (clienteEmail != null) {
            throw new BadRequestException("Cliente já cadastrado com este e-mail");
        }

        if (clienteCpf != null) {
            throw new BadRequestException("Cliente já cadastrado com este Cpf");
        }

        return clienteRepository.save(ClienteEntity.builder()
                .cpf(clienteDto.getCpf())
                .nome(clienteDto.getNome())
                .telefone(clienteDto.getTelefone())
                .email(clienteDto.getEmail())
                .build());
    }

    public List<ClienteEntity> getListarCliente() {
        return clienteRepository.findAll();
    }

    public ClienteEntity getBuscarPorId(UUID id) throws NotFoundException {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com o ID: " + id));
    }

    public Page<ClienteEntity> listarClientesPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
        return clienteRepository.findAll(pageable);
    }

    public ClienteEntity getBuscarPorCpf(String cpf) throws NotFoundException {
        return clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com o ID: " + cpf));
    }

    public List<ClienteEntity> getBuscarPorNome(String nome) throws NotFoundException {
         List<ClienteEntity> cliente = clienteRepository.findByNomeContainingIgnoreCase(nome);

        if (cliente.isEmpty()) {
            throw new NotFoundException("Cliente não encontrado com o nome: " + nome);
        }
        return cliente;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletar(UUID id) throws NotFoundException {
        ClienteEntity cliente = getBuscarPorId(id);
        List<OrdemServicoEntity> ordensServico = ordemServicoRepository.findByClienteId(id);

        for (OrdemServicoEntity ordemServico : ordensServico) {
            pecaUtilizadaRepository.deleteByOrdemServicoId_Id(ordemServico.getId());
            servicoRealizadoRepository.deleteByOrdemServicoId_Id(ordemServico.getId());
        }

        ordemServicoRepository.deleteAll(ordensServico);
        equipamentoRepository.deleteByClienteId(id);
        clienteRepository.delete(cliente);
    }


    public ClienteEntity atualizarCliente(UUID id, ClienteDto dto) throws NotFoundException {
        ClienteEntity clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        clienteExistente.setNome(dto.getNome());
        clienteExistente.setCpf(dto.getCpf());
        clienteExistente.setTelefone(dto.getTelefone());
        clienteExistente.setEmail(dto.getEmail());

        return clienteRepository.save(clienteExistente);
    }


}
