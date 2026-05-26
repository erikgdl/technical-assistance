package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.database.model.ClienteEntity;
import com.example.assistencia_tecnica.dto.ClienteDto;
import com.example.assistencia_tecnica.exception.BadRequestException;
import com.example.assistencia_tecnica.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /*
    @GetMapping
    public ResponseEntity<List<ClienteEntity>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteEntity> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }
    */

}
