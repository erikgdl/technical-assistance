package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.database.model.ServicoEntity;
import com.example.assistencia_tecnica.database.model.TecnicoEntity;
import com.example.assistencia_tecnica.dto.ServicoDto;
import com.example.assistencia_tecnica.dto.TecnicoDto;
import com.example.assistencia_tecnica.service.ServicoService;
import com.example.assistencia_tecnica.service.TecnicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/servico")
@RequiredArgsConstructor
@Validated
public class ServicoController {

    private final ServicoService serviceService;

    @PostMapping
    public ResponseEntity<ServicoEntity> cadastrarServico(@RequestBody @Validated ServicoDto dto) {
        ServicoEntity novoServico = serviceService.criarServico(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoServico);
    }

}
