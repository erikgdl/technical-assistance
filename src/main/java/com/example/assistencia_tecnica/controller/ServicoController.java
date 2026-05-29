package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.database.model.ServicoEntity;
import com.example.assistencia_tecnica.dto.ServicoDto;
import com.example.assistencia_tecnica.exception.NotFoundException;
import com.example.assistencia_tecnica.service.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/servico")
@RequiredArgsConstructor
@Validated
public class ServicoController {

    private final ServicoService servicoService;

    @PostMapping
    public ResponseEntity<ServicoEntity> cadastrarServico(@RequestBody @Validated ServicoDto dto) {
        ServicoEntity novoServico = servicoService.criarServico(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoServico);
    }

    @PutMapping("/atualiza/{id}")
    public ResponseEntity<ServicoEntity> atualizarPeco(@PathVariable Long id,
                                                    @RequestBody @Valid ServicoDto dto) throws NotFoundException {
        ServicoEntity atualizarServico = servicoService.atualizarServico(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(atualizarServico);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<ServicoEntity>> listarPecas() {
        List<ServicoEntity> servico = servicoService.getListarServico();
        return ResponseEntity.status(HttpStatus.OK).body(servico);
    }

    @GetMapping
    public ResponseEntity<Page<ServicoEntity>> listarTodosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(servicoService.listarTodosPaginado(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoEntity> buscarPorId(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(servicoService.buscarPorId(id));
    }
}
