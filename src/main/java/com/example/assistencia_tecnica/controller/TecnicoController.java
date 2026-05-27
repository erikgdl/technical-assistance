package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.database.model.TecnicoEntity;
import com.example.assistencia_tecnica.dto.TecnicoDto;
import com.example.assistencia_tecnica.exception.NotFoundException;
import com.example.assistencia_tecnica.service.TecnicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/todos")
    public ResponseEntity<List<TecnicoEntity>> listarTodosClientes() {
        List<TecnicoEntity> tecnico = tecnicoService.getListarTecnico();
        return ResponseEntity.status(HttpStatus.OK).body(tecnico);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TecnicoEntity> getBuscarPorId(@PathVariable("id") Long id) throws NotFoundException {
        TecnicoEntity tecnicoId = tecnicoService.getBuscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(tecnicoId);
    }

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<TecnicoEntity> getBuscarPorMatricula(@PathVariable("matricula") String matricula) throws NotFoundException {
        TecnicoEntity tecnicoMatricula = tecnicoService.getBuscarPorMatricula(matricula);
        return ResponseEntity.status(HttpStatus.OK).body(tecnicoMatricula);
    }


}
