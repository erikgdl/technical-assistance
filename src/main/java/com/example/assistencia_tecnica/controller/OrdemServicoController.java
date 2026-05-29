package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.database.model.OrdemServicoEntity;
import com.example.assistencia_tecnica.database.model.PecaUtilizadaEntity;
import com.example.assistencia_tecnica.database.model.ServicoRealizadoEntity;
import com.example.assistencia_tecnica.dto.*;
import com.example.assistencia_tecnica.enums.StatusServicoEnum;
import com.example.assistencia_tecnica.exception.BadRequestException;
import com.example.assistencia_tecnica.exception.NotFoundException;
import com.example.assistencia_tecnica.service.OrdemServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/v1/ordens-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    @PostMapping
    public ResponseEntity<OrdemServicoEntity> abrirOS(@RequestBody @Valid OrdemServicoDto dto) throws NotFoundException, BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemServicoService.abrirOS(dto));
    }


    @GetMapping("/todos")
    public ResponseEntity<List<OrdemServicoEntity>> listarPecas() {
        List<OrdemServicoEntity> os = ordemServicoService.getListarOrdemServico();
        return ResponseEntity.status(HttpStatus.OK).body(os);
    }

    @GetMapping
    public ResponseEntity<Page<OrdemServicoEntity>> listarTodasPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ordemServicoService.listarTodasPaginado(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoEntity> buscarPorId(@PathVariable("id") UUID id) throws NotFoundException {
        return ResponseEntity.ok(ordemServicoService.buscarPorId(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<OrdemServicoEntity>> buscarPorStatus(
            @PathVariable("status") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws BadRequestException {
        StatusServicoEnum statusEnum;
        try {
            statusEnum = StatusServicoEnum.valueOf(status.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Status inválido: " + status);
        }

        return ResponseEntity.ok(ordemServicoService.buscarPorStatus(statusEnum, page, size));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<OrdemServicoEntity>> buscarPorClienteId(@PathVariable("clienteId") UUID clienteId) {
        return ResponseEntity.ok(ordemServicoService.buscarPorClienteId(clienteId));
    }

    @GetMapping("/tecnico/{tecnicoId}")
    public ResponseEntity<List<OrdemServicoEntity>> buscarPorTecnicoId(@PathVariable("tecnicoId") Long tecnicoId) {
        return ResponseEntity.ok(ordemServicoService.buscarPorTecnicoId(tecnicoId));
    }

    @GetMapping("/{id}/pecas")
    public ResponseEntity<List<PecaUtilizadaEntity>> buscarPecasDaOs(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(ordemServicoService.buscarPecasDaOs(id));
    }

    @GetMapping("/{id}/servicos")
    public ResponseEntity<List<ServicoRealizadoEntity>> buscarServicosDaOs(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(ordemServicoService.buscarServicosDaOs(id));
    }

    @PatchMapping("/{id}/iniciar-analise/{tecnicoId}")
    public ResponseEntity<OrdemServicoEntity> iniciarAnalise(
            @PathVariable("id") UUID id,
            @PathVariable("tecnicoId") Long tecnicoId) throws NotFoundException, BadRequestException {
        return ResponseEntity.ok(ordemServicoService.iniciarAnalise(id, tecnicoId));
    }

    @PatchMapping("/{id}/laudo")
    public ResponseEntity<OrdemServicoEntity> registrarLaudo(
            @PathVariable("id") UUID id,
            @RequestBody @Valid RegistrarLaudoDto dto) throws NotFoundException, BadRequestException {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOrdemServico(@PathVariable("id") UUID id) throws NotFoundException {
        ordemServicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
