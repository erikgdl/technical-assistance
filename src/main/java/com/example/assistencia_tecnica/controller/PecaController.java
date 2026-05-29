package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.database.model.PecaEntity;
import com.example.assistencia_tecnica.dto.PecaDto;
import com.example.assistencia_tecnica.exception.NotFoundException;
import com.example.assistencia_tecnica.service.PecaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/atualiza/{id}")
    public ResponseEntity<PecaEntity> atualizarPeco(@PathVariable Long id,
                                                          @RequestBody @Valid PecaDto dto) throws NotFoundException {
        PecaEntity atualizarPeca = pecaService.atualizarPeca(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(atualizarPeca);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<PecaEntity>> listarPecas() {
        List<PecaEntity> peca = pecaService.getListarPeca();
        return ResponseEntity.status(HttpStatus.OK).body(peca);
    }

    @GetMapping
    public ResponseEntity<Page<PecaEntity>> listarTodasPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(pecaService.listarTodasPaginado(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PecaEntity> buscarPorId(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(pecaService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPeca(@PathVariable("id") Long id) throws NotFoundException {
        pecaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
