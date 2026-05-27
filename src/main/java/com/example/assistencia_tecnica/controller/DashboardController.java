package com.example.assistencia_tecnica.controller;

import com.example.assistencia_tecnica.database.model.PecaEntity;
import com.example.assistencia_tecnica.dto.DashboardResponseDto;
import com.example.assistencia_tecnica.dto.PecaDto;
import com.example.assistencia_tecnica.exception.NotFoundException;
import com.example.assistencia_tecnica.service.DashboardService;
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
@RequestMapping("v1/dashboard")
@RequiredArgsConstructor
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponseDto> obterDashboard() {
        DashboardResponseDto dados = dashboardService.obterDadosDashboard();
        return ResponseEntity.ok(dados);
    }

}
