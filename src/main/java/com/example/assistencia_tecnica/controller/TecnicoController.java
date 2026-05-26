package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.database.model.TecnicoEntity;
import com.example.assistencia_tecnica.dto.TecnicoDto;
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
@RequestMapping("v1/tecnico")
@RequiredArgsConstructor
@Validated
public class TecnicoController {

    private final TecnicoService tecnicoService;

    @PostMapping
    public ResponseEntity<TecnicoEntity> cadastrarEquipamento(@RequestBody @Validated TecnicoDto dto) throws Exception {
        TecnicoEntity novoTecnico = tecnicoService.criarTecnico(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoTecnico);
    }

}
