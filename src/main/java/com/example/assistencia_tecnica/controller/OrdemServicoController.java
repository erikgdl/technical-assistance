package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.database.model.OrdemServicoEntity;
import com.example.assistencia_tecnica.dto.*;
import com.example.assistencia_tecnica.service.OrdemServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/ordens-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    @PostMapping
    public ResponseEntity<OrdemServicoEntity> abrirOS(@RequestBody @Valid OrdemServicoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemServicoService.abrirOS(dto));
    }

    @PatchMapping("/{id}/iniciar-analise/{tecnicoId}")
    public ResponseEntity<OrdemServicoEntity> iniciarAnalise(
            @PathVariable("id") UUID id,
            @PathVariable("tecnicoId") Long tecnicoId) {
        return ResponseEntity.ok(ordemServicoService.iniciarAnalise(id, tecnicoId));
    }

    @PatchMapping("/{id}/laudo")
    public ResponseEntity<OrdemServicoEntity> registrarLaudo(
            @PathVariable("id") UUID id,
            @RequestBody @Valid RegistrarLaudoDto dto) {
        return ResponseEntity.ok(ordemServicoService.registrarLaudo(id, dto));
    }

    @PostMapping("/{id}/pecas")
    public ResponseEntity<OrdemServicoEntity> adicionarPeca(
            @PathVariable("id") UUID id,
            @RequestBody @Valid AdicionarPecaDto dto) throws Exception {
        return ResponseEntity.ok(ordemServicoService.adicionarPeca(id, dto));
    }

    @PostMapping("/{id}/servicos")
    public ResponseEntity<OrdemServicoEntity> adicionarServico(
            @PathVariable("id") UUID id,
            @RequestBody @Valid AdicionarServicoDto dto) throws Exception {
        return ResponseEntity.ok(ordemServicoService.adicionarServico(id, dto));
    }

    @PatchMapping("/{id}/enviar-aprovacao")
    public ResponseEntity<OrdemServicoEntity> enviarParaAprovacao(@PathVariable("id") UUID id) throws Exception {
        return ResponseEntity.ok(ordemServicoService.enviarParaAprovacao(id));
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<OrdemServicoEntity> aprovarOS(@PathVariable("id") UUID id) throws Exception {
        return ResponseEntity.ok(ordemServicoService.aprovarOS(id));
    }

    @PatchMapping("/{id}/iniciar-manutencao")
    public ResponseEntity<OrdemServicoEntity> iniciarManutencao(@PathVariable("id") UUID id) throws Exception {
        return ResponseEntity.ok(ordemServicoService.iniciarManutencao(id));
    }

    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<OrdemServicoEntity> reabrirManutencao(@PathVariable("id") UUID id) throws Exception {
        return ResponseEntity.ok(ordemServicoService.reabrirManutencao(id));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<OrdemServicoEntity> concluirManutencao(@PathVariable("id") UUID id) throws Exception {
        return ResponseEntity.ok(ordemServicoService.concluirManutencao(id));
    }

    @PatchMapping("/{id}/entregar")
    public ResponseEntity<OrdemServicoEntity> entregarEquipamento(@PathVariable("id") UUID id) throws Exception {
        return ResponseEntity.ok(ordemServicoService.entregarEquipamento(id));
    }
}