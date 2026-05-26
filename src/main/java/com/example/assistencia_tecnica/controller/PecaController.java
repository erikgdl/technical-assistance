package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.database.model.PecaEntity;
import com.example.assistencia_tecnica.database.model.TecnicoEntity;
import com.example.assistencia_tecnica.dto.PecaDto;
import com.example.assistencia_tecnica.dto.TecnicoDto;
import com.example.assistencia_tecnica.service.PecaService;
import com.example.assistencia_tecnica.service.TecnicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/peca")
@RequiredArgsConstructor
@Validated
public class PecaController {

    private final PecaService pecaService;

    @PostMapping
    public ResponseEntity<PecaEntity> cadastrarPeca(@RequestBody @Valid PecaDto dto) {
        PecaEntity novaPeca = pecaService.criarPeca(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPeca);
    }
}
