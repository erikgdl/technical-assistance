package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.dto.EquipamentoDto;
import com.example.assistencia_tecnica.service.EquipamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/equipamento")
@RequiredArgsConstructor
@Validated
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarEquipamento(@RequestBody @Validated EquipamentoDto equipamentoDto) {
        equipamentoService.criarEquipamento(equipamentoDto);
    }
}
