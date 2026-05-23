package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.dto.ClienteDto;
import com.example.assistencia_tecnica.exception.BadRequestException;
import com.example.assistencia_tecnica.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/cliente")
@RequiredArgsConstructor
@Validated
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarCliente(@Valid @RequestBody ClienteDto clienteDto) throws BadRequestException {
        clienteService.criarCliente(clienteDto);
    }
}
