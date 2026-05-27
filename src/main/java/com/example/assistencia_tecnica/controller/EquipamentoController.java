package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.database.model.EquipamentoEntity;
import com.example.assistencia_tecnica.dto.EquipamentoDto;
import com.example.assistencia_tecnica.exception.NotFoundException;
import com.example.assistencia_tecnica.service.EquipamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/equipamento")
@RequiredArgsConstructor
@Validated
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    @PostMapping
    public ResponseEntity<EquipamentoEntity> criarEquipamento(@RequestBody @Validated EquipamentoDto dto) throws NotFoundException {
        EquipamentoEntity novoEquipamento = equipamentoService.criarEquipamento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEquipamento);
    }


    @GetMapping("/todos")
    public ResponseEntity<List<EquipamentoEntity>> listarTodosEquipamentos() {
        List<EquipamentoEntity> equipamento = equipamentoService.getListarEquipamento();
        return ResponseEntity.status(HttpStatus.OK).body(equipamento);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoEntity> getBuscarPorId(@PathVariable("id") UUID id) throws NotFoundException {
        EquipamentoEntity equipamento = equipamentoService.getBuscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(equipamento);
    }

    @GetMapping
    public ResponseEntity<Page<EquipamentoEntity>> listarTodosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(equipamentoService.listarClientesPaginado(page, size));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<EquipamentoEntity>> buscarPorClienteId(@PathVariable("clienteId") UUID clienteId) throws NotFoundException {
        return ResponseEntity.ok(equipamentoService.buscarPorClienteId(clienteId));
    }

    // Rastreio pelo IMEI
    @GetMapping("/numero-serie/{numeroSerie}")
    public ResponseEntity<EquipamentoEntity> buscarPorNumeroSerie(@PathVariable("numeroSerie") String numeroSerie) throws NotFoundException {
        return ResponseEntity.ok(equipamentoService.buscarPorNumeroSerie(numeroSerie));
    }

}
