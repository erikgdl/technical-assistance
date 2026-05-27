package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.database.model.ClienteEntity;
import com.example.assistencia_tecnica.dto.ClienteDto;
import com.example.assistencia_tecnica.exception.BadRequestException;
import com.example.assistencia_tecnica.exception.NotFoundException;
import com.example.assistencia_tecnica.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/cliente")
@RequiredArgsConstructor
@Validated
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteEntity> cadastrarCliente(@RequestBody @Valid ClienteDto dto) throws BadRequestException {
        ClienteEntity novoCliente = clienteService.criarCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<ClienteEntity>> listarTodosClientes() {
        List<ClienteEntity> cliente = clienteService.getListarCliente();
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClienteEntity> getBuscarPorId(@PathVariable("id") UUID id) throws NotFoundException {
        ClienteEntity cliente = clienteService.getBuscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @GetMapping
    public ResponseEntity<Page<ClienteEntity>> listarClientesPaginado(
            @RequestParam(defaultValue = "0") int page, // Se não mandar nada, puxa a página 0
            @RequestParam(defaultValue = "10") int size // Se não mandar nada, puxa 10 itens
    ) {
        Page<ClienteEntity> clientes = clienteService.listarClientesPaginado(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(clientes);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteEntity> getBuscarPorCpf(@PathVariable("cpf") String cpf) throws NotFoundException {
        ClienteEntity cliente = clienteService.getBuscarPorCpf(cpf);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @GetMapping("/busca")
    public ResponseEntity<List<ClienteEntity>> buscarPorNome(@RequestParam("nome") String nome) throws NotFoundException {
        List<ClienteEntity> clientes = clienteService.getBuscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable("id") UUID id) throws NotFoundException {
        clienteService.deletar(id);
        // Retorna o status 204 (No Content)
        return ResponseEntity.noContent().build();
    }

}
